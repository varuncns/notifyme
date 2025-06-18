package com.notifyme.service;

import com.notifyme.dto.NotificationEventDTO;
import com.notifyme.entity.NotificationLog;
import com.notifyme.repository.NotificationLogRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationSender {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final NotificationLogRepository logRepository;

    public void sendEmail(NotificationEventDTO event) {
        // Build log entity
        NotificationLog.NotificationLogBuilder logBuilder = NotificationLog.builder()
                .recipientEmail(event.getRecipientEmail())
                .subject(event.getSubject())
                .message(event.getMessage())
                .type("EMAIL")
                .retryCount(0)
                .timestamp(LocalDateTime.now());

        try {
            // Create HTML email
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(event.getRecipientEmail());
            helper.setSubject(event.getSubject());
            helper.setFrom("your_email@gmail.com"); // Replace with your sender address

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("message", event.getMessage());

            String htmlContent = templateEngine.process("email-template", context);
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(mimeMessage);
            log.info("Email sent to {}", event.getRecipientEmail());

            logBuilder.status("SENT").error(null);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", event.getRecipientEmail(), e.getMessage());
            logBuilder.status("FAILED").error(e.getMessage());
        }

        // Save notification log to PostgreSQL
        logRepository.save(logBuilder.build());
    }
}
