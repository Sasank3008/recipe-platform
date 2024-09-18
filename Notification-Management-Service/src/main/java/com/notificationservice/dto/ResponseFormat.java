package com.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFormat {
    private String status,message;
}

