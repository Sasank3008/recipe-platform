package com.user.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String statusMessage;
    private String error;
}
