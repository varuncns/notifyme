package com.notifyme.dto;

import java.io.Serializable;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEventDTO  implements Serializable  {
    private String recipientEmail;
    private String subject;
    private String message;
    private String type; // e.g., "EMAIL" or "SMS" (used in future milestones)
}
