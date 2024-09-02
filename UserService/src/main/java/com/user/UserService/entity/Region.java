package com.user.UserService.entity;





import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


   // @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "country_id")
    private Country country;
    @JsonIgnore
    @OneToMany(mappedBy = "region")
    private List<User> users;

    public Region(long l, String regionName) {
        this.id=l;
        this.name=regionName;
    }

}

