package com.recipe.recipeservice.exception;

public class CuisineNotFoundException extends RuntimeException {
    public CuisineNotFoundException(String message) {
        super(message);
    }
}
