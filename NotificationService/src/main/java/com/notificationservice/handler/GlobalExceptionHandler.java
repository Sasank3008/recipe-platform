package com.notificationservice.handler;


import com.notificationservice.dto.ResponseFormatDTO;
import com.notificationservice.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleEmptyCountryListException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");
        ResponseFormatDTO responseFormatDTO = ResponseFormatDTO.builder().status("401").message(errorMessage).build();
        return ResponseEntity.badRequest().body(responseFormatDTO);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex){
        ResponseFormatDTO responseFormatDTO = ResponseFormatDTO.builder().status("400").message(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(responseFormatDTO);
    }
}
