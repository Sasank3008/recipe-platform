package com.user.UserService.entity;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "country")
    private List<User> users;

    @OneToMany(mappedBy = "country")
    private List<Region> regions;


    public Country(Long id,String name){
        this.id=id;
        this.name=name;
    }
}

