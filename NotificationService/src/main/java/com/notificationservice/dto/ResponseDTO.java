package com.notificationservice.dto;

import com.notificationservice.entity.Notifications;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO {
    private String status;
    private String message;
    private List<Notifications> data;
}
