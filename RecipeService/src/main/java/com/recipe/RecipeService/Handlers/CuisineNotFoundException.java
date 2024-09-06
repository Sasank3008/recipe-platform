package com.recipe.RecipeService.Handlers;

public class CuisineNotFoundException extends RuntimeException {
    public CuisineNotFoundException(String message) {
        super(message);
    }
}
