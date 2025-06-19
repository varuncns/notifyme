package com.notifyme.consumer;

import com.notifyme.config.RabbitMQConfig;
import com.notifyme.dto.NotificationEventDTO;
import com.notifyme.service.NotificationSender;
import com.notifyme.service.NotificationSenderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationSenderFactory senderFactory;

    @RabbitListener(queues = RabbitMQConfig.NOTIFY_QUEUE)
    public void consume(NotificationEventDTO event) {
        log.info("📩 Received notification event: {}", event);

        NotificationSender sender = senderFactory.getSender(event.getType());
        if (sender != null) {
            sender.send(event);
        } else {
            log.warn(" No sender found for type: {}", event.getType());
        }
    }
}
