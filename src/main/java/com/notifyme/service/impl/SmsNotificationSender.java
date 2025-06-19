package com.notifyme.service.impl;

import com.notifyme.dto.NotificationEventDTO;
import com.notifyme.entity.NotificationLog;
import com.notifyme.repository.NotificationLogRepository;
import com.notifyme.service.NotificationSender;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsNotificationSender implements NotificationSender {

    private final NotificationLogRepository logRepository;

    @Value("${twilio.from.phone}")
    private String fromPhone;

    @Override
    public void send(NotificationEventDTO event) {
        NotificationLog.NotificationLogBuilder logBuilder = NotificationLog.builder()
                .recipientEmail(event.getPhoneNumber())  // Store phone temporarily here
                .subject(event.getSubject())
                .message(event.getMessage())
                .type("SMS")
                .retryCount(0)
                .timestamp(LocalDateTime.now());

        try {
            Message.creator(
                new PhoneNumber(event.getPhoneNumber()),
                new PhoneNumber(fromPhone),
                event.getMessage()
            ).create();

            log.info("SMS sent to {}", event.getPhoneNumber());
            logBuilder.status("SENT").error(null);
        } catch (Exception e) {
            log.error("Failed to send SMS: {}", e.getMessage());
            logBuilder.status("FAILED").error(e.getMessage());
        }

        logRepository.save(logBuilder.build());
    }

    @Override
    public String getType() {
        return "SMS";
    }
}
