package com.user.UserService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String country;
    private String region;

    @Lob
    @Column(name = "profile_image",columnDefinition = "LONGBLOB")
    private byte[] profileImage;
    @Transient
    private String profileImageUrl;
    @JsonIgnore
    private String password;

    public String getProfileImageUrl() {
        if (profileImage != null) {
            return "/user/profile-image/" + id;
        }
        return null;
    }



}
