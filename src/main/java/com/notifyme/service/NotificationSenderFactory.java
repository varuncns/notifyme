package com.notifyme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationSenderFactory {
    private final List<NotificationSender> senders;
    private final Map<String, NotificationSender> senderMap = new HashMap<>();
    @PostConstruct
    public void init() {
        for (NotificationSender sender : senders) {
            senderMap.put(sender.getType().toUpperCase(), sender);
        }
    }
    public NotificationSender getSender(String type) {
        return senderMap.get(type.toUpperCase());
    }
}

