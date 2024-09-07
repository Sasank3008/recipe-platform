package com.user.userservice.handler;

import com.user.userservice.dto.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        return new ResponseEntity<>
                (
                        ApiResponse.builder()
                                .response(exception.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString())
                                .timestamp(LocalDateTime.now())
                                .build()
                        ,
                        HttpStatus.BAD_REQUEST
                );
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
