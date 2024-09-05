package com.recipe.recipeservice.handlers;

public class CuisineNotFoundException extends RuntimeException {
    public CuisineNotFoundException(String message) {
        super(message);
    }
}
