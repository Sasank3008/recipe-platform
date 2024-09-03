package com.user.UserService.controllerTests;

import com.user.UserService.Handler.InvalidPasswordException;
import com.user.UserService.Handler.UserNotFoundException;
import com.user.UserService.controller.UserController;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.UserEntity;
import com.user.UserService.repository.UserRepository;
import com.user.UserService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private Long userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = 1L;
    }

    @Test
    void saveUser_ShouldReturnCreatedStatus() {
        UserDTO userDTO = new UserDTO();
        ResponseEntity<?> response = userController.saveUser(userDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User Registered Successfully", response.getBody());
        verify(userService, times(1)).createUser(userDTO);
    }

    @Test
    void getUserById_ShouldReturnUserDTO() throws UserNotFoundException {
        UserDTO userDTO = new UserDTO();
        when(userService.getUser(userId)).thenReturn(userDTO);
        ResponseEntity<?> response = userController.getUserById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void getUserById_ShouldThrowUserNotFoundException() throws UserNotFoundException {
        when(userService.getUser(userId)).thenThrow(new UserNotFoundException("User not found"));
        assertThrows(UserNotFoundException.class, () -> userController.getUserById(userId));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws UserNotFoundException {
        UserDTO userDTO = new UserDTO();
        when(userService.getUser(userId)).thenReturn(userDTO);
        doNothing().when(userService).updateUser(userDTO, userId);
        ResponseEntity<?> response = userController.updateUser(userDTO, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void getProfileImage_ShouldReturnImage() throws UserNotFoundException {
        UserEntity userEntity = new UserEntity();
        userEntity.setProfileImage(new byte[0]);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        ResponseEntity<byte[]> response = userController.getProfileImage(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(new byte[0], response.getBody());
    }

    @Test
    void updatePassword_ShouldReturnSuccess() throws UserNotFoundException, InvalidPasswordException {
        PasswordDTO passwordDTO = new PasswordDTO();
        doNothing().when(userService).updatePassword(passwordDTO, userId);
        ResponseEntity<?> response = userController.updatePassword(passwordDTO, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password Updated Successfully", response.getBody());
    }
}
