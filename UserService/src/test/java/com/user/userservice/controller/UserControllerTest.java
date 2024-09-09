package com.user.userservice.controller;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.dto.UserLoginDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import com.user.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User user;
    private UserLoginDTO userLoginDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setRole("USER");
        userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("user@example.com");
        userLoginDTO.setPassword("password");
    }

    @Test
    void loginSuccess() throws IncorrectPasswordException {
        Pair<String, User> responsePair = Pair.of("dummyToken", user);
        when(userService.login(userLoginDTO)).thenReturn(responsePair);
        ResponseEntity<UserResponseDTO> responseEntity = userController.login(userLoginDTO);
        assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
        UserResponseDTO responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("dummyToken", responseBody.getToken());
        assertEquals("User Login Successfully", responseBody.getMessage());
        assertEquals(1L, responseBody.getUserId());
        assertEquals("USER", responseBody.getRole());
        verify(userService).login(userLoginDTO);
    }

    @Test
    void loginFailureIncorrectPassword() throws IncorrectPasswordException {
        when(userService.login(userLoginDTO)).thenThrow(new IncorrectPasswordException("Incorrect password provided"));
        Exception exception = assertThrows(IncorrectPasswordException.class, () -> userController.login(userLoginDTO));
        assertEquals("Incorrect password provided", exception.getMessage());
        verify(userService).login(userLoginDTO);
    }
}