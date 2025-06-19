package com.notifyme.service;

import com.notifyme.dto.NotificationEventDTO;

public interface NotificationSender {
    void send(NotificationEventDTO event);
    String getType(); // Should return "EMAIL", "SMS", etc.
}
