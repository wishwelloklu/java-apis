package com.example.java_tutorial.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sourceEmail;

    private final org.thymeleaf.spring6.SpringTemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender, org.thymeleaf.spring6.SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(sourceEmail);

            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String templateName, org.thymeleaf.context.Context context) {
        try {
            jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
            org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(
                    mimeMessage, "UTF-8");

            try {
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setFrom(sourceEmail);
                String htmlContent = templateEngine.process(templateName, context);
                helper.setText(htmlContent, true);
                mailSender.send(mimeMessage);
            } catch (jakarta.mail.MessagingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (MailException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
