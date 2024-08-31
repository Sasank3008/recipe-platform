package com.user.UserService.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Table
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalTime timeOfRegistration;
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
}
