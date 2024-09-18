package com.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyUserdto {

    @NotBlank(message = "User email is required for sending the notification")
    private String email;

    @NotBlank(message = "Message is required to notify")
    private String message;
}
