package com.user.UserService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {

    private String oldPassword;
    @Pattern(regexp = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$/", message = "Password should contain atleast 1 lowercase letter,1 uppercase letter, 1 special character, 1 digit and min-length of 5")
    private String newPassword;
    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;

}
