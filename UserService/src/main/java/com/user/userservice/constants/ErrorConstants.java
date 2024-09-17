package com.user.userservice.constants;

public class ErrorConstants {

    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found for user ID: ";
    public static final String IMAGE_URL_NOT_FOUND = "Image URL not found for user ID: ";
    public static final String IMAGE_FILE_NOT_FOUND = "Image file not found at path: ";
    public static final String FILE_MUST_NOT_BE_EMPTY = "File must not be empty";
    public static final String FILE_NAME_MUST_NOT_BE_NULL = "File name must not be null";
    public static final String INVALID_FILE_TYPE = "Invalid file type. Only PNG, JPG, JPEG, and SVG are allowed.";
    public static final String INVALID_PASSWORD = "Invalid Password";
    public static final String PASSWORDS_DO_NOT_MATCH = "New password and Confirm password do not match";
    public static final String EMAIL_EXISTS = "Email already exists";
    public static final String COUNTRY_NOT_FOUND = "No country found with ID: ";

    private ErrorConstants() {
        // Prevent instantiation
    }
}
