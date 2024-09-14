package com.recipe.recipeservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CuisineDTO {
    private Long id;
    private String name;
    private boolean isEnabled;
    private String imageUrl;

}