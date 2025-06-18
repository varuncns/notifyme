package com.notifyme.consumer;

import com.notifyme.config.RabbitMQConfig;
import com.notifyme.dto.NotificationEventDTO;
import com.notifyme.service.EmailNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {
	
	private final EmailNotificationSender emailSender;

	
    @RabbitListener(queues = RabbitMQConfig.NOTIFY_QUEUE)
    public void consume(NotificationEventDTO event) {
        log.info("Received Notification Event: {}", event);
        
        if ("EMAIL".equalsIgnoreCase(event.getType())) {
            emailSender.sendEmail(event);
        } else {
            log.warn(" Unsupported notification type: {}", event.getType());
        }
    }
}
