package com.demo.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.demo.exception.CustomException;
import com.demo.model.Student;
import com.demo.service.StudentDataService;

@RestController
@RequestMapping(value = "/api")
public class StudentDataController {
	
	private final Logger logger = LoggerFactory.getLogger(StudentDataController.class);

	
	@Autowired
	StudentDataService studentDataService;
	

	@GetMapping("/students")
	public ResponseEntity<ResponseWrapperList> getAllStudents(@RequestParam(required = false) String title)  throws Exception{
			
		List<Student> students = studentDataService.list();

		if (students.isEmpty()) {
			throw new CustomException(1001, "Data Not Found");
		}

		return new ResponseEntity<>(new ResponseWrapperList(students), HttpStatus.OK);
		
	}

	@GetMapping("/students/{id}")
	public ResponseEntity<ResponseWrapper> getStudentById(@PathVariable("id") String id) throws Exception {
		Optional<Student> studentData = studentDataService.get(id);

		if (studentData.isPresent()) {
			return new ResponseEntity<>(new ResponseWrapper(studentData.get()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseWrapper(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/students")
	public ResponseEntity<ResponseWrapper> createStudent(@RequestBody Student student) throws Exception {
		try {
			String id = UUID.randomUUID().toString();
			Student _student = new Student(id, student.getName(), student.getMale(), student.getGrade(), StudentDataService.SUBMITTED_STATUS); 
			_student.setCreatedDate(new Date());		
			studentDataService.save(_student, true);
			
			return new ResponseEntity<>(new ResponseWrapper(_student), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/students/{id}")
	public ResponseEntity<ResponseWrapper> updateStudent(@PathVariable("id") String id, @RequestBody Student student) throws Exception {
		Optional<Student> studentData = studentDataService.get(id);

		if (studentData.isPresent()) {
			Student _student = studentData.get();
			_student.setName(student.getName());
			_student.setMale(student.getMale());
			_student.setGrade(student.getGrade());
			_student.setLastModifiedDate(new Date());
			return new ResponseEntity<>(new ResponseWrapper(studentDataService.save(_student, false)), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseWrapper(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/students/{id}")
	public ResponseEntity<ResponseWrapper> deleteStudent(@PathVariable("id") String id) throws Exception {
		try {
			studentDataService.delete(id);
			return new ResponseEntity<>(new ResponseWrapper(), HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
