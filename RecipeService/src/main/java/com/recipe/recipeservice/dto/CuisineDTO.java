package com.recipe.recipeservice.dto;



public class CuisineDTO {
    private Long id;
    private String name;
    private boolean isEnabled=true;

    // Constructors
    public CuisineDTO() {}

    public CuisineDTO(Long id, String name, boolean isEnabled) {
        this.id = id;
        this.name = name;
        this.isEnabled = isEnabled;
    }

    // Getters and Setters
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

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}