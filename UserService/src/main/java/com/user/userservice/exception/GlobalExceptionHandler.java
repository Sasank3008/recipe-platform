package com.user.userservice.exception;

import com.user.userservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CuisineIdNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCuisineIdNotFoundException(CuisineIdNotFoundException ex) {
        ApiResponse response = ApiResponse.builder()
                .response(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(DuplicateCuisineException.class)
    public ResponseEntity<ApiResponse> handleDuplicateCuisineException(DuplicateCuisineException ex) {
        ApiResponse response = ApiResponse.builder()
                .response(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}