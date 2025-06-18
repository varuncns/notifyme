package com.notifyme.controller;

import com.notifyme.config.RabbitMQConfig;
import com.notifyme.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody NotificationEvent event) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.NOTIFY_EXCHANGE,
            RabbitMQConfig.NOTIFY_ROUTING_KEY,
            event
        );
        return ResponseEntity.ok("Notification published to queue!");
    }
}
