package com.recipe.recipeservice.exception;

import com.recipe.recipeservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(CuisineNotFoundException.class)
        public ResponseEntity<String> handleCuisineNotFoundException(CuisineNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        @ExceptionHandler(DuplicateCuisineException.class)
        public ResponseEntity<String> handleDuplicateCuisineException(DuplicateCuisineException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGeneralException(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            errorMessage.append(" ").append(message).append(". ");
        });
        ApiResponse response = ApiResponse.builder()
                .response(errorMessage.toString().trim())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
              ErrorResponse errorResponse = ErrorResponse.builder()
                .error(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .statusMessage(HttpStatus.BAD_REQUEST.toString())
                .build();
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}

