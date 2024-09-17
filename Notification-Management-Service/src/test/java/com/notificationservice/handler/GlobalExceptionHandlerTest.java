package com.notificationservice.handler;

import com.notificationservice.dto.ResponseFormatDTO;
import com.notificationservice.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }


    @Test
    void testHandleMethodArgumentNotValidExceptionWithNoErrors() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleEmptyCountryListException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseFormatDTO responseFormatDTO = (ResponseFormatDTO) responseEntity.getBody();
        assertEquals("401", responseFormatDTO.getStatus());
        assertEquals("Validation failed", responseFormatDTO.getMessage());
    }

    @Test
    void testHandleNotFoundException() {
        // Arrange
        NotFoundException ex = new NotFoundException("Resource not found");

        // Act
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleNotFoundException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ResponseFormatDTO responseFormatDTO = (ResponseFormatDTO) responseEntity.getBody();
        assertEquals("400", responseFormatDTO.getStatus());
        assertEquals("Resource not found", responseFormatDTO.getMessage());
    }
}
