package com.recipe.recipeservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class CuisineDTO {
    private Long id;
    private String name;
    private boolean isEnabled=true;


}