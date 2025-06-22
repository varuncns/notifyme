package com.notifyme.consumer;

import com.notifyme.dto.NotificationEventDTO;
import com.notifyme.entity.NotificationLog;
import com.notifyme.repository.NotificationLogRepository;
import com.notifyme.service.NotificationSender;
import com.notifyme.service.NotificationSenderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationSenderFactory senderFactory;
    private final RabbitTemplate rabbitTemplate;
    private final NotificationLogRepository logRepository;

    private static final int MAX_RETRIES = 3;
    private static final String EXCHANGE = "notifyme.notification.exchange";

    @RabbitListener(queues = "notifyme.email.queue")
    public void handleEmail(@Payload NotificationEventDTO event) {
        handleNotification(event, "email");
    }

    @RabbitListener(queues = "notifyme.sms.queue")
    public void handleSms(@Payload NotificationEventDTO event) {
        handleNotification(event, "sms");
    }

    @RabbitListener(queues = "notifyme.push.queue")
    public void handlePush(@Payload NotificationEventDTO event) {
        handleNotification(event, "push");
    }

    private void handleNotification(NotificationEventDTO event, String type) {
        log.info("Received {} notification event: {}", type.toUpperCase(), event);

        NotificationSender sender = senderFactory.getSender(type);

        if (sender == null) {
            log.warn("No sender found for type: {}", type);
            return;
        }

        try {
            sender.send(event);
            saveLog(event, "SENT", event.getRetryCount(), false, null);
        } catch (Exception e) {
            int retry = event.getRetryCount() + 1;
            boolean maxRetryReached = retry >= MAX_RETRIES;
            saveLog(event, "FAILED", retry, maxRetryReached, e.getMessage());

            if (!maxRetryReached) {
                event.setRetryCount(retry);

                String retryRoutingKey = switch (type.toLowerCase()) {
                    case "email" -> "notify.email.retry";
                    case "sms" -> "notify.sms.retry";
                    case "push" -> "notify.push.retry";
                    default -> "notify.unknown.retry";
                };

                rabbitTemplate.convertAndSend(EXCHANGE, retryRoutingKey, event);
                log.warn("Retry {}/{} scheduled for {} notification via {}.", retry, MAX_RETRIES, type, retryRoutingKey);
            } else {
                log.error("Max retries reached for {} notification. Dropping message: {}", type, event);
            }
        }
    }

    private void saveLog(NotificationEventDTO event, String status, int retryCount, boolean maxRetryReached, String errorMessage) {
        NotificationLog logEntry = NotificationLog.builder()
                .recipientEmail(event.getRecipientEmail())
                .phoneNumber(event.getPhoneNumber())
                .type(event.getType())
                .message(event.getMessage())
                .status(status)
                .retryCount(retryCount)
                .maxRetryReached(maxRetryReached)
                .error(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();

        logRepository.save(logEntry);
    }
}
