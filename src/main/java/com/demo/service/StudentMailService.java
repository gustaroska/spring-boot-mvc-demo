package com.demo.service;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.demo.helper.KafkaDataMessage;
import com.demo.helper.TopicConfig;
import com.demo.model.Student;
import com.demo.repository.StudentDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StudentMailService {

	private final Logger logger = LoggerFactory.getLogger(StudentDataService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	StudentDataRepository studentDataRepository; // Redis

	@KafkaListener(topics = TopicConfig.STUDENTS_OUTBOUND, groupId = "group_id")
	public void studentDataSendEmail(String message) throws IOException {
		logger.info(String.format("#### -> Consumed message -> %s", message));

		KafkaDataMessage kdm = new ObjectMapper().readValue(message, KafkaDataMessage.class);

		Optional<Student> student = studentDataRepository.findById(kdm.getId());

		if (student.isPresent()) {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo("gustaroska@gmail.com", "gustaroska@outlook.com");
			msg.setFrom("admin@donatur.id");
			msg.setSubject("Update Data Student");
			msg.setText(kdm.getMessage() + "\n\n" + student.get().toString());

			javaMailSender.send(msg);
		}


	}
}
