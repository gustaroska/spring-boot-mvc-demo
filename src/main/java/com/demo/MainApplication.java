package com.demo;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.demo.service.AppMailService;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories("com.demo.repository")
public class MainApplication extends SpringBootServletInitializer implements CommandLineRunner {
	
	@Autowired
    private AppMailService appMailService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
	

	@Override
    public void run(String... args) throws MessagingException, IOException {

		//mailService.sendEmail();
		
		
		 
    }
}
