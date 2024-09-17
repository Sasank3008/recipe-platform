package com.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyUserDTO {

    @NotBlank(message = "User ID is required for sending the notification")
    private String userId;

    @NotBlank(message = "Message is required to notify")
    private String message;
}
