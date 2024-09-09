package com.user.userservice.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

    @InjectMocks
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void shouldHandleExpiredJwtException() throws IOException {
        AuthenticationException authException = mock(AuthenticationException.class);
        when(authException.getCause()).thenReturn(new ExpiredJwtException(null, null, "JWT token is expired"));
        jwtAuthenticationEntryPoint.commence(request, response, authException);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        writer.flush();
        assertEquals("{\"error\": \"JWT token is expired\"}", stringWriter.toString().trim());
    }

    @Test
    void shouldHandleInvalidJwtException() throws IOException {
        AuthenticationException authException = new AuthenticationException("Invalid JWT token") {};
        jwtAuthenticationEntryPoint.commence(request, response, authException);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        writer.flush();
        assertEquals("{\"error\": \"Invalid JWT token: Invalid JWT token\"}", stringWriter.toString().trim());
    }
}