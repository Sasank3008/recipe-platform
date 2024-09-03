package com.user.UserService.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {


    private Long id;
    private String firstName;
    private String lastName;
    private String country;
    private String region;
    private String profileImageUrl;
    private String password;
}
