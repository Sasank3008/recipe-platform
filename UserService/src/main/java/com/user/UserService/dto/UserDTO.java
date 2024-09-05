package com.user.UserService.dto;

import com.user.UserService.entity.Country;
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
    private Country country;
    private String region;
    private String profileImageUrl;
    private String password;
}
