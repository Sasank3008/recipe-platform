package com.user.UserService.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
            return ResponseEntity.
                    status(HttpStatus.BAD_REQUEST)
                    .body(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    ResponseEntity<String>constraintViolationException(SQLIntegrityConstraintViolationException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
    @ExceptionHandler(EmailNotFoundException.class)
    ResponseEntity<String>emailNotFoundException(EmailNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    @ExceptionHandler(UserIdNotFoundException.class)
    ResponseEntity<String>userIdNotFoundException(UserIdNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
