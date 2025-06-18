package com.notifyme.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientEmail;
    private String subject;
    
    @Column(length = 5000)
    private String message;

    private String type;    // EMAIL, SMS, etc.
    private String status;  // SENT, FAILED
    private String error;

    private int retryCount;

    private LocalDateTime timestamp;
}
