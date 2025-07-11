package com.example.hospital_management.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendAppointmentConfirmation(String to, String name, LocalDate date, int queueNumber) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("date", date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        context.setVariable("queueNumber", queueNumber);

        String htmlContent = templateEngine.process("email/appointment-confirmation.html", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("mhuynhk1005@gmail.com", "Bệnh viện CodeGym");
            helper.setTo(to);
            helper.setSubject("Xác nhận đặt lịch khám bệnh");
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

