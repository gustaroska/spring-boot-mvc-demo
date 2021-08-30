package com.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AppMailService {
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	
	public void sendEmail() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("gustaroska@gmail.com", "gustaroska@outlook.com");
        msg.setFrom("admin@donatur.id");
        msg.setSubject("Aplikasi Contoh Fungsi");
        msg.setText("Notifikasi aplikasi dijalankan pada " + new Date());

        javaMailSender.send(msg);

    }
	
	public static void main(String[] args) {
		new AppMailService().sendEmail();
	}
}
