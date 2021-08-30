package com.demo.controllers;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mail")
public class MailController {

	@Autowired
    private JavaMailSender javaMailSender;

    @PostMapping(value = "/send")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) throws Exception{
    	sendEmailWithAttachment(message);
    }
    
    void sendEmailWithAttachment(String message) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("refit.darfiti@pertamina.com");
        helper.setFrom(new InternetAddress("\"Field TDM\" <Field.TDM@pertamina.com>"));
        helper.setSubject("Testing from Spring Boot");

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("<h1>"+message+"</h1>", true);

        helper.addAttachment("my_photo.png", new ClassPathResource("public/images/pertamina.png"));

        javaMailSender.send(msg);

    }
}
