package com.recipe.RecipeService.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "cuisines")
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isEnabled=true;
    public Cuisine() {
        this.isEnabled = true;
    }

    public Cuisine(Long id, String name, boolean isEnabled) {
        this.id = id;
        this.name = name;
        this.isEnabled = isEnabled;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    // Ensure this method is correctly named as 'setEnabled'
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    // Constructors, Getters, and Setters
}
