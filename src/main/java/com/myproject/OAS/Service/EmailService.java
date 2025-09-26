package com.myproject.OAS.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Synchronous method (optional, existing)
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true = HTML enabled

        mailSender.send(message);
    }

    // 🔹 Asynchronous method
    @Async
    public void sendHtmlEmailAsync(String to, String subject, String htmlBody) {
        try {
            sendHtmlEmail(to, subject, htmlBody);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    
    @Async
    public void sendHtmlEmailBcc(List<String> bccList, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo("adevraj934@gmail.com"); // ya koi dummy "to", kyunki BCC me bhej rahe
            helper.setBcc(bccList.toArray(new String[0])); // BCC recipients
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
