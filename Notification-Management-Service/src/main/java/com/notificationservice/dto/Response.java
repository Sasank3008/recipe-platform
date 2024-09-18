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
public class Response {
    private String status,message;
    private List<Notifications> data;
}
