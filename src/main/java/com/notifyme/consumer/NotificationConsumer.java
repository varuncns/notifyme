package com.notifyme.consumer;

import com.notifyme.config.RabbitMQConfig;
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
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationSenderFactory senderFactory;
    private final RabbitTemplate rabbitTemplate;
    private final NotificationLogRepository logRepository;

    private static final int MAX_RETRIES = 3;

    @RabbitListener(queues = RabbitMQConfig.NOTIFY_QUEUE)
    public void consume(NotificationEventDTO event) {
        log.info("Received notification event: {}", event);

        NotificationSender sender = senderFactory.getSender(event.getType());

        if (sender == null) {
            log.warn("No sender found for type: {}", event.getType());
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
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.NOTIFY_EXCHANGE,
                        RabbitMQConfig.RETRY_ROUTING_KEY,
                        event
                );
                log.warn("Retry {}/{} scheduled for {}.", retry, MAX_RETRIES, event.getType());
            } else {
                log.error("Max retries reached for {}. Dropping message.", event);
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
