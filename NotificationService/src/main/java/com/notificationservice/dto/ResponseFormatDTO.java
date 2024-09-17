package com.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFormatDTO {
    private String status;
    private String message;
}

