package com.notifyme.controller;

import com.notifyme.dto.NotificationEventDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "notifyme.notification.exchange";

    @PostMapping
    public ResponseEntity<String> sendNotification(@Valid @RequestBody NotificationEventDTO event) {
        String type = event.getType(); // e.g., email, sms, push

        if (type == null || type.isBlank()) {
            return ResponseEntity.badRequest().body("Notification type must be provided");
        }

        String routingKey = String.format("notify.%s.send", type.toLowerCase());

        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, event);
        return ResponseEntity.ok("Notification queued successfully to: " + routingKey);
    }

}
