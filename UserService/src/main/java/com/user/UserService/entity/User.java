package com.user.UserService.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonIgnore
    private Country country;

    @ManyToOne
    @JoinColumn(name = "region_id")

    @JsonIgnore
    private Region region;
}

