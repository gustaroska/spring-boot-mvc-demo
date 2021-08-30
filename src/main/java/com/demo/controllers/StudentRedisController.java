package com.demo.controllers;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Student;
import com.demo.redis.StudentRedisRepository;
import com.demo.service.StudentDataService;

@RestController
@RequestMapping(value = "/api/redis")
public class StudentRedisController {
	
	private final Logger logger = LoggerFactory.getLogger(StudentRedisController.class);

	
	@Autowired
	StudentDataService studentDataService;
	
	@Autowired
	StudentRedisRepository studentRedisRepository;
	

	@GetMapping("/students/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable("id") String id) throws Exception {
		Optional<Student> studentData = studentRedisRepository.findById(id);

		if (studentData.isPresent()) {
			return new ResponseEntity<>(studentData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/students")
	public ResponseEntity<Student> createStudent(@RequestBody Student student) throws Exception {
		try {
			String id = UUID.randomUUID().toString();
			Student _student = new Student(id, student.getName(), student.getMale(), student.getGrade(), StudentDataService.DRAFTED_STATUS); 
			_student.setCreatedDate(new Date());
			studentRedisRepository.save(_student);
			
			return new ResponseEntity<>(_student, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/students/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable("id") String id, @RequestBody Student student) throws Exception {
		Optional<Student> studentData = studentRedisRepository.findById(id);

		if (studentData.isPresent()) {
			Student _student = studentData.get();
			_student.setName(student.getName());
			_student.setMale(student.getMale());
			_student.setGrade(student.getGrade());
			return new ResponseEntity<>(studentRedisRepository.save(_student), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/students/{id}")
	public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") String id) throws Exception {
		try {
			studentRedisRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Moving data from redis to database
	 * 
	 * @param id
	 * @param student
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/students/submit/{id}")
	public ResponseEntity<Student> submitStudent(@PathVariable("id") String id) throws Exception {
		Optional<Student> studentData = studentRedisRepository.findById(id);

		if (studentData.isPresent()) {
			Student _student = studentData.get();
			_student.setStatus(StudentDataService.SUBMITTED_STATUS);
			_student.setLastModifiedDate(new Date());
			return new ResponseEntity<>(studentDataService.save(_student), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
