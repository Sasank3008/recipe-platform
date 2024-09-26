package com.recipe.recipeservice.constants;

public class ErrorConstants {

    public static final String INVALID_FILE_TYPE = "Invalid file type. Only PNG, JPG, JPEG, and SVG are allowed.";
    public static final String INVALID_FILE_NAME="File must have a valid name.";
    public static final String RECIPE_ID_NOT_FOUND="No Recipe found with ID";
    public static final String INVALID_INPUTS = "Provided inputs are not valid";

    private ErrorConstants() {
        // Prevent instantiation
    }
}

