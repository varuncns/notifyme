package com.notifyme.service;

import com.notifyme.dto.NotificationEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NotificationSenderFactoryTest {

    private NotificationSenderFactory factory;

    static class DummySender implements NotificationSender {
        private final String type;

        DummySender(String type) {
            this.type = type;
        }

        @Override
        public void send(NotificationEventDTO event) {
            // no-op
        }

        @Override
        public String getType() {
            return type;
        }
    }

    @BeforeEach
    void setUp() {
        factory = new NotificationSenderFactory(Arrays.asList(
                new DummySender("EMAIL"),
                new DummySender("SMS")
        ));
        factory.init();
    }

    @Test
    void returnsNullWhenTypeIsNull() {
        NotificationSender sender = factory.getSender(null);
        assertNull(sender);
    }

    @Test
    void returnsSenderForKnownType() {
        NotificationSender sender = factory.getSender("email");
        assertNotNull(sender);
        assertEquals("EMAIL", sender.getType());
    }
}
