package com.recipe.recipeservice.handlers;

public class DuplicateCuisineException extends RuntimeException{
    public DuplicateCuisineException(String message) {
        super(message);
    }
}
