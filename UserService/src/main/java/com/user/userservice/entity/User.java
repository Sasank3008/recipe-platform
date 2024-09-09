package com.user.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String region;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    private String email;
    private String password;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;


}
