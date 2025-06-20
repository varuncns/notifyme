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
    
    @Column(name = "phone_number")
    private String phoneNumber;

    
    @Column(length = 5000)
    private String message;

    private String type;    // EMAIL, SMS, etc.
    private String status;  // SENT, FAILED
    private String error;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "max_retry_reached")
    private Boolean maxRetryReached;


    private LocalDateTime timestamp;
}
