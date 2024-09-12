package com.user.userservice.exception;

import com.user.userservice.dto.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {

        ApiResponse response = ApiResponse.builder().response(ex.getMessage()).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ApiResponse> handleInvalidPasswordException(IncorrectPasswordException ex) {
        ApiResponse response = ApiResponse.builder().response(ex.getMessage()).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<ApiResponse> handleJwtException(JwtException e, HttpServletRequest request) {
        ApiResponse response = ApiResponse.builder().response(e.getMessage()).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class) // Handle org.springframework.security.access.AccessDeniedException
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        ApiResponse response = ApiResponse.builder()
                .response(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<ApiResponse> userIdNotFoundException(UserIdNotFoundException ex){
        ApiResponse response=ApiResponse.builder()
                .response(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(CountryAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleCountryAlreadyExistsException(CountryAlreadyExistsException exception)
    {
        return new ResponseEntity<>
                (
                        ApiResponse.builder()
                                .response(exception.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build()
                        ,
                        HttpStatus.IM_USED
                );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();

        // Extract and format validation errors
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            errorMessage.append(" ").append(message).append(". ");
        });
        // Build ApiResponse with the formatted error messages
        ApiResponse response = ApiResponse.builder()
                .response(errorMessage.toString().trim())
                .timestamp(LocalDateTime.now())
                .build();
        // Return the response with BAD_REQUEST status
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponse> constraintViolationException(SQLIntegrityConstraintViolationException ex) {
        ApiResponse response=ApiResponse.builder()
                .response(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiResponse> passwordMismatchException(PasswordMismatchException ex){
        ApiResponse response=ApiResponse.builder()
                .response(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public  ResponseEntity<ApiResponse> emailNotFoundException(EmailNotFoundException ex){
        ApiResponse response=ApiResponse.builder()
                .response(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

}