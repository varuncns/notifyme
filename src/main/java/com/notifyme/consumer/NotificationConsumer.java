package com.notifyme.consumer;

import com.notifyme.config.RabbitMQConfig;
import com.notifyme.dto.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.NOTIFY_QUEUE)
    public void consume(NotificationEvent event) {
        log.info("Received Notification Event: {}", event);
        // We will later trigger email sending here (in Milestone 3)
    }
}
