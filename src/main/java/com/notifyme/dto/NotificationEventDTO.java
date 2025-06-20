package com.notifyme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class NotificationEventDTO {

    private String recipientEmail;

    private String phoneNumber;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    @NotBlank(message = "Type must be provided (EMAIL or SMS)")
    private String type;

    private int retryCount = 0;

    @AssertTrue(message = "Email or phone number is required")
    public boolean isRecipientValid() {
        return (recipientEmail != null && !recipientEmail.isBlank())
                || (phoneNumber != null && !phoneNumber.isBlank());
    }
}
