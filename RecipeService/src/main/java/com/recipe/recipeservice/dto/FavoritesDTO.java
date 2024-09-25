package com.recipe.recipeservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritesDTO {
    private Long userId;
    private Long recipeId;
}
