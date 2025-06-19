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
    private final NotificationLogRepository logRepository;

    @Override
    public void send(NotificationEventDTO event) {
        NotificationLog.NotificationLogBuilder logBuilder = NotificationLog.builder()
                .recipientEmail(event.getRecipientEmail())
                .subject(event.getSubject())
                .message(event.getMessage())
                .type("EMAIL")
                .retryCount(0)
                .timestamp(LocalDateTime.now());

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(event.getRecipientEmail());
            helper.setSubject(event.getSubject());
            helper.setFrom("your_email@gmail.com");

            Context context = new Context();
            context.setVariable("message", event.getMessage());
            String html = templateEngine.process("email-template", context);
            helper.setText(html, true);

            mailSender.send(mimeMessage);
            log.info("Email sent to {}", event.getRecipientEmail());

            logBuilder.status("SENT").error(null);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
            logBuilder.status("FAILED").error(e.getMessage());
        }

        logRepository.save(logBuilder.build());
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}
