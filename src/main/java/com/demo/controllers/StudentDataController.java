package com.demo.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.ResponseWrapper;
import com.demo.ResponseWrapperList;
import com.demo.model.Student;
import com.demo.service.StudentDataService;

@RestController
@RequestMapping(value = "/api")
public class StudentDataController {
	
	private final Logger logger = LoggerFactory.getLogger(StudentDataController.class);

	private final static String SUBMIT_ACTION = "submit";
	
	private final static String EDIT_ACTION = "edit";
	
	private final static String UNDO_DELETE_ACTION = "undo";
	
	@Autowired
	StudentDataService studentDataService;
	
	@Autowired
	CacheManager cacheManager;
	

	@GetMapping
	public ResponseEntity<ResponseWrapperList> getListByParam(@RequestParam(required = false) String title)  throws Exception{
			
		List<Student> students = studentDataService.list();

		if (students.isEmpty()) {
			return new ResponseEntity<>(new ResponseWrapperList(), HttpStatus.OK);
		}

		return new ResponseEntity<>(new ResponseWrapperList(students), HttpStatus.OK);
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseWrapper> getById(@PathVariable("id") String id) throws Exception {
		Student studentData = studentDataService.get(id);

		if (studentData != null) {
			return new ResponseEntity<>(new ResponseWrapper(studentData), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseWrapper(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<ResponseWrapper> create(
			@RequestParam(name = "action", required = false) String action, // available action: SUBMIT
			@RequestBody Student student) throws Exception {
		try {
			
			Student data = studentDataService.save(validate(student, new Student(UUID.randomUUID().toString()), action));
			
			return new ResponseEntity<>(new ResponseWrapper(data), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseWrapper> update(@PathVariable("id") String id, 
			@RequestParam(name = "action", required = false) String action, // available action: SUBMIT | EDIT
			@RequestBody Student student) throws Exception {
		
		ValueWrapper cacheValue = cacheManager.getCache(StudentDataService.CACHE).get(id);
		
		Optional<Student> studentData = Optional.ofNullable((Student)cacheValue.get());
		
		if (!studentData.isPresent()) {
			
			studentData = Optional.ofNullable(studentDataService.get(id));
		}

		if (studentData.isPresent()) {
			
			return new ResponseEntity<>(new ResponseWrapper(studentDataService.save(validate(student, studentData.get(), action))), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseWrapper(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") String id,
			@RequestParam(name = "action", required = false) String action // available action: UNDO_DELETE
			) throws Exception {
		try {
			
			if(action != null && action.equals(UNDO_DELETE_ACTION)) {
				
				ValueWrapper cacheValue = cacheManager.getCache(StudentDataService.CACHE).get(id);
				
				if (cacheValue != null) {
					
					studentDataService.save((Student)cacheValue.get());
				}
				
				
				
				return new ResponseEntity<>(new ResponseWrapper(), HttpStatus.NO_CONTENT);	
			}
			
			studentDataService.delete(id);
			
			return new ResponseEntity<>(new ResponseWrapper(), HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


    private Student validate(Student _student, Student data, String action) {
    	Student student = data;
    	

    	if(_student != null) {

	    	student.setName(_student.getName());
	    	student.setMale(_student.getMale());
	    	student.setGrade(_student.getGrade()); // check if user granted to change this value
	
	    	if(data.getCreatedDate() != null) {
	    		student.setLastModifiedDate(new Date());	
	    	}
	    	
    	}
    	
    	if(action == null) {
    		if(student.getStatus() == null) student.setStatus(StudentDataService.DRAFTED_STATUS);
		}else {
			
			if(action.equals(SUBMIT_ACTION)) { // make sure user login is authorized for this action
				student.setStatus(StudentDataService.SUBMITTED_STATUS); 
			}	
			
			if(action.equals(EDIT_ACTION)) { // make sure user login is authorized for this action
				student.setStatus(StudentDataService.DRAFTED_STATUS); 
			}	
			

		}
    	return student;
    }
}
