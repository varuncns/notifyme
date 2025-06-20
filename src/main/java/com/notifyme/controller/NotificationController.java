package com.notifyme.controller;

import com.notifyme.config.RabbitMQConfig;
import com.notifyme.dto.NotificationEventDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<String> sendNotification(@Valid @RequestBody NotificationEventDTO event) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.NOTIFY_EXCHANGE,
            RabbitMQConfig.NOTIFY_ROUTING_KEY,
            event
        );
        return ResponseEntity.ok("Notification queued successfully");
    }
}
