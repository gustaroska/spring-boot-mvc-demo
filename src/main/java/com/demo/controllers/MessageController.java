package com.demo.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


@Controller
public class MessageController {

	
	@MessageMapping("/hello")
	@SendToUser("/queue/reply")
	public String say(@Payload String name) throws Exception {
		System.out.println("Hello " + name);
		return "Hello " + name;
	}

}
