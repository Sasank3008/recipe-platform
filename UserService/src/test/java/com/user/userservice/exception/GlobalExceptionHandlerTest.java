package com.user.userservice.exception;

import com.user.userservice.dto.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;
    @Mock
    private HttpServletRequest request;  // Mocked request for JwtException
    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleUsernameNotFoundException() {
        UsernameNotFoundException exception = new UsernameNotFoundException("User not found");
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleUsernameNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getResponse());
    }

    @Test
    void shouldHandleIncorrectPasswordException() {
        IncorrectPasswordException exception = new IncorrectPasswordException("Incorrect password");
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleInvalidPasswordException(exception);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Incorrect password", response.getBody().getResponse());
    }

    @Test
    void shouldHandleJwtException() {
        JwtException exception = new JwtException("Invalid JWT");
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleJwtException(exception, request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid JWT", response.getBody().getResponse());
    }

    @Test
    void shouldHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleAccessDeniedException(exception);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Access denied", response.getBody().getResponse());
    }
}