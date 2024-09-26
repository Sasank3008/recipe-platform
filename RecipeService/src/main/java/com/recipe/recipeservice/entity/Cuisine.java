package com.recipe.recipeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
@Table(name = "cuisine")
@Data
@NoArgsConstructor
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isEnabled=true;
    public Cuisine(Long id, String name, boolean isEnabled) {
        this.id = id;
        this.name = name;
        this.isEnabled = isEnabled;
    }
    @OneToMany(mappedBy = "cuisine")
    @JsonIgnore
    private List<Recipe> recipes;
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    // Constructors, Getters, and Setters
}
