package com.user.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
public class ApiResponse
{
    private String response;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

//   // public ApiResponse() {
//        this.timestamp = LocalDateTime.now();
//    }
}
