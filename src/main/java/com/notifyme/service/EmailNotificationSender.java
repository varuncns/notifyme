package com.notifyme.service;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.notifyme.dto.NotificationEventDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationSender {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(NotificationEventDTO event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(event.getRecipientEmail());
            helper.setSubject(event.getSubject());
            helper.setFrom("your_email@gmail.com");

            // Prepare dynamic HTML content
            Context context = new Context();
            context.setVariable("message", event.getMessage());

            String htmlContent = templateEngine.process("email-template", context);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
            log.info(" HTML email sent to {}", event.getRecipientEmail());

        } catch (Exception e) {
            log.error(" Failed to send HTML email: {}", e.getMessage());
        }
    }
}
