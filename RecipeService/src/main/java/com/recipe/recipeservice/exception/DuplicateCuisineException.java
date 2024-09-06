package com.recipe.recipeservice.exception;

public class DuplicateCuisineException extends RuntimeException{
    public DuplicateCuisineException(String message) {
        super(message);
    }
}
