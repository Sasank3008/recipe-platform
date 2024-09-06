package com.recipe.RecipeService.Handlers;

public class DuplicateCuisineException extends RuntimeException{
    public DuplicateCuisineException(String message) {
        super(message);
    }
}
