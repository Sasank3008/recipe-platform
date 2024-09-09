package com.user.userservice.service;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.dto.UserLoginDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final String token = "mocked-token";
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private CustomUserDetailsService userDetailsService;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;
    private UserLoginDTO userLoginDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encryptedPassword");
        user.setEnabled(true);
        userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("test@example.com");
        userLoginDTO.setPassword("password");
    }

    @Test
    void testSuccessfulLogin() throws IncorrectPasswordException {
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateToken(any(Authentication.class))).thenReturn(token);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        Pair<String, User> result = userService.login(userLoginDTO);
        assertEquals(token, result.getLeft());
        assertEquals(user, result.getRight());
    }

    @Test
    void testNoUsernameFound() {
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User Not found"));
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.login(userLoginDTO));
        assertEquals("User Not found", exception.getMessage());
    }

    @Test
    void testWrongPassword() {
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertThrows(IncorrectPasswordException.class, () -> userService.login(userLoginDTO));
    }

    @Test
    void testTokenGeneration() throws IncorrectPasswordException {
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateToken(any(Authentication.class))).thenReturn(token);
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        Pair<String, User> result = userService.login(userLoginDTO);
        assertEquals(token, result.getLeft());
    }
}