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

    private static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";

    @Override
    public void send(NotificationEventDTO event) {
        String phone = event.getPhoneNumber();

        // Validate phone number format
        if (phone == null || !phone.matches(PHONE_REGEX)) {
            log.warn("Invalid phone number format: {}", phone);
            throw new IllegalArgumentException("Invalid phone number: " + phone);
        }

        try {
            Message.creator(
                new PhoneNumber(phone),
                new PhoneNumber(fromPhone),
                event.getMessage()
            ).create();

            log.info("SMS sent to {}", phone);
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", phone, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public String getType() {
        return "SMS";
    }
}
