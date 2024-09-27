package com.recipe.recipeservice.service;

import com.recipe.recipeservice.dto.ApiResponse;
import com.recipe.recipeservice.dto.FavouritesRecipeResponse;
import com.recipe.recipeservice.exception.DuplicateResourceException;
import com.recipe.recipeservice.exception.ResourceNotFoundException;

public interface FavouritesService {
    ApiResponse addFavoriteRecipe(String userId, Long recipeId) throws ResourceNotFoundException, DuplicateResourceException;
    ApiResponse deleteFavoriteRecipe(String userId, Long recipeId) throws ResourceNotFoundException;
    FavouritesRecipeResponse getFavoriteRecipes(String userId) throws ResourceNotFoundException;
}
