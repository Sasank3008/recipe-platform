package com.user.userservice.exception;

import com.user.userservice.dto.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        ApiResponse response = ApiResponse.builder().response(e.getMessage()).timestamp(LocalDateTime.now()).build();

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
}