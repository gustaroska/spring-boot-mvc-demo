package com.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.demo.helper.KafkaDataMessage;
import com.demo.helper.TopicConfig;
import com.demo.mapper.StudentMapper;
import com.demo.model.Student;
import com.demo.redis.StudentRedisRepository;
import com.demo.repository.StudentDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StudentDataService {

	private final Logger logger = LoggerFactory.getLogger(StudentDataService.class);
	
	public final static String CACHE = "students";
	
	public final static String DRAFTED_STATUS = "drafted";
	
	public final static String SUBMITTED_STATUS = "submitted";
	
	@Autowired
	CacheManager cacheManager;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	StudentRedisRepository studentRedisRepository; // Redis

	@Autowired
	StudentDataRepository studentDataRepository; // JPA Hibernate
	
	@Autowired
	StudentMapper studentMapper; // MyBatis Mapper

	@KafkaListener(topics = TopicConfig.STUDENTS_INBOUND, groupId = "group_id")
	public void consume(String message) throws IOException {
		logger.info(String.format("#### -> Consumed message -> %s", message));

		// write anything need to do
	}
	
	public void evictAllCaches() {
		logger.info("evictAllCaches");
		
	    cacheManager.getCacheNames().stream()
	      .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}
	
	@Scheduled(fixedRate = 1000 * 60 * 60)
	public void evictAllcachesAtIntervals() {
	    evictAllCaches();
	}

	@CachePut(value=CACHE, key="#student.id")
	public Student save(Student student) throws Exception {
		Student data = student;
		try {
				
				if(student.getStatus().equals(SUBMITTED_STATUS))	{	
					data = studentDataRepository.save(student);
				}
		
				// start prepare pub
				String status = "Penambahan";
				if (data.getLastModifiedDate() != null) {
					
					status = "Perubahan";
				}
				String body = "Data student telah berhasil " + (status.equals("Penambahan") ? "ditambahkan" : "diubah")
						+ " pada " + new Date();
				String jsonMsg = new ObjectMapper()
						.writeValueAsString(new KafkaDataMessage(Student.class.getName(), student.getId(), body));
				//this.kafkaTemplate.send(TopicConfig.STUDENTS_OUTBOUND, jsonMsg);
				// end prepare pub
		
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return data;
	}

	@Cacheable(CACHE)
	public Student get(String id) throws Exception {
		Optional<Student> result = studentDataRepository.findById(id);
		if(result.isPresent()) {
			return result.get();
		}
		return null;
	}
	
	public List<Student> list() throws Exception {
		List<Student> students = new ArrayList<Student>();
		studentDataRepository.findAll().forEach(students::add);
		return students;
	}

	@CachePut(value=CACHE, key="#id")
	public Student delete(String id) throws Exception {
		
		Optional<Student> result = studentDataRepository.findById(id);
		
		if(result.isPresent()) {
			
			Student data = result.get();
			data.setDeletedDate(new Date());
			
			studentMapper.deleteById(id);
			
			
			
			return data;
		}
		return null;
		
	}
}
