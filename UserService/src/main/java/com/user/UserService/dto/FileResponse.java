package com.user.UserService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponse {

    private String fileName;
    private String message;

}
