package com.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.demo.helper.KafkaDataMessage;
import com.demo.helper.TopicConfig;
import com.demo.model.Student;
import com.demo.redis.StudentRedisRepository;
import com.demo.repository.StudentDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StudentDataService {

	private final Logger logger = LoggerFactory.getLogger(StudentDataService.class);
	
	private final static String CACHE = "students";
	
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

	@CachePut(CACHE)
	public Student save(Student student, boolean append) throws Exception {
		
		try {
				Student data = studentDataRepository.save(student);
		
				// start prepare pub
				String status = "Penambahan";
				if (!append) {
					
					// manually remove cache
					cacheManager.getCache(CACHE).evict(student.getId());
					
					status = "Perubahan";
				}
				String body = "Data student telah berhasil " + (status.equals("Penambahan") ? "ditambahkan" : "diubah")
						+ " pada " + new Date();
				String jsonMsg = new ObjectMapper()
						.writeValueAsString(new KafkaDataMessage(Student.class.getName(), student.getId(), body));
				//this.kafkaTemplate.send(TopicConfig.STUDENTS_OUTBOUND, jsonMsg);
				// end prepare pub
		
				return data;
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}

	@Cacheable(value=CACHE, key = "#id")
	public Optional<Student> get(String id) throws Exception {
		return studentDataRepository.findById(id);
	}
	
	public List<Student> list() throws Exception {
		List<Student> students = new ArrayList<Student>();
		studentDataRepository.findAll().forEach(students::add);
		return students;
	}

	@CacheEvict(CACHE)
	public void delete(String id) throws Exception {
		studentDataRepository.deleteById(id);
	}
}
