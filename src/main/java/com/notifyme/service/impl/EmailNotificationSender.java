package com.notifyme.service.impl;

import com.notifyme.dto.NotificationEventDTO;
import com.notifyme.entity.NotificationLog;
import com.notifyme.repository.NotificationLogRepository;
import com.notifyme.service.NotificationSender;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    @Override
    public void send(NotificationEventDTO event) {
        String email = event.getRecipientEmail();

        // ✅ Format validation
        if (email == null || !email.matches(EMAIL_REGEX)) {
            log.warn("Invalid email format: {}", email);
            throw new IllegalArgumentException("Invalid email: " + email);
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(event.getSubject());
            helper.setFrom("your_email@gmail.com");

            Context context = new Context();
            context.setVariable("message", event.getMessage());
            String html = templateEngine.process("email-template", context);
            helper.setText(html, true);

            mailSender.send(mimeMessage);
            log.info("Email sent to {}", email);
        } catch (Exception e) {
            log.error("Email sending failed: {}", e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}
