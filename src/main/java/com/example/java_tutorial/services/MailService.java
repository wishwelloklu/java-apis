package com.example.java_tutorial.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sourceEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your One-Time Password (OTP) is: " + otp);
        message.setFrom(sourceEmail);

        mailSender.send(message);
    }
}
