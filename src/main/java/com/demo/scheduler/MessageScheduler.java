package com.demo.scheduler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class MessageScheduler {

	@Autowired
	private SimpMessagingTemplate template;
	

	@Scheduled(fixedRate = 60*1000) // 1000 = 1 second
	private void trigger() {
		// TODO Auto-generated method stub
		//System.out.println(new Date());
		template.convertAndSend("/queue/reply", "I'm triggered from server <"+new Date()+">");
	}
}
