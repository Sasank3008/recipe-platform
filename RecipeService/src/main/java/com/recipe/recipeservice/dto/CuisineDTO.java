package com.recipe.recipeservice.dto;
import lombok.Data;

@Data
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



    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}