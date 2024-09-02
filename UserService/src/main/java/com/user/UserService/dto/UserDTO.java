package com.user.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


        private String firstName;
        private String lastName;
        private String email;
        private Boolean enabled;
//        private CountryDTO country;
//        private RegionDTO region;
        private CountryDTO Country;
        private RegionDTO region;
        private String password;




    // private LocalTime timeOfRegistration;



        // Getters and Setters

}
