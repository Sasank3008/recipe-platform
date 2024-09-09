package com.user.UserService;

import com.user.UserService.controller.UserController;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserUpdateDTO;
import com.user.UserService.exception.InvalidPasswordException;
import com.user.UserService.exception.UserNotFoundException;
import com.user.UserService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserControllerTests{

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private String path = "user/images/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetUserById_UserNotFoundException() throws UserNotFoundException {
        when(userService.getUser(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        // Verifying
        assertThrows(UserNotFoundException.class, () -> userController.getUserById(1L));
    }


    @Test
    void testUpdateUser_UserNotFoundException() throws UserNotFoundException {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(); // Populate with test data
        doThrow(new UserNotFoundException("User not found")).when(userService).updateUser(userUpdateDTO, 1L);

        // Verifying
        assertThrows(UserNotFoundException.class, () -> userController.updateUser(userUpdateDTO, 1L));
    }


 

    @Test
    void testUpdatePassword_Success() throws UserNotFoundException, InvalidPasswordException {
        PasswordDTO passwordDTO = new PasswordDTO(); // Populate with test data

        // Calling the API
        ResponseEntity<?> response = userController.updatePassword(passwordDTO, 1L);

        // Verifying
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password Updated Successfully", response.getBody());
        verify(userService).updatePassword(passwordDTO, 1L);
    }

    @Test
    void testUpdatePassword_UserNotFoundException() throws UserNotFoundException, InvalidPasswordException {
        PasswordDTO passwordDTO = new PasswordDTO(); // Populate with test data
        doThrow(new UserNotFoundException("User not found")).when(userService).updatePassword(passwordDTO, 1L);

        // Verifying
        assertThrows(UserNotFoundException.class, () -> userController.updatePassword(passwordDTO, 1L));
    }

    @Test
    void testUpdatePassword_InvalidPasswordException() throws UserNotFoundException, InvalidPasswordException {
        PasswordDTO passwordDTO = new PasswordDTO(); // Populate with test data
        doThrow(new InvalidPasswordException("Invalid password")).when(userService).updatePassword(passwordDTO, 1L);

        // Verifying
        assertThrows(InvalidPasswordException.class, () -> userController.updatePassword(passwordDTO, 1L));
    }

    @Test
    void testGetProfileImage_Success() throws UserNotFoundException, IOException {
        byte[] image = new byte[]{1, 2, 3}; // Replace with your actual image data

        when(userService.getUserProfileImage(anyLong())).thenReturn(image);

        // Calling the API
        ResponseEntity<?> response = userController.getProfileImage(1L);

        // Verifying
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
        assertArrayEquals(image, (byte[]) response.getBody());
    }

    @Test
    void testGetProfileImage_UserNotFoundException() throws UserNotFoundException, IOException {
        when(userService.getUserProfileImage(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        // Verifying
        assertThrows(UserNotFoundException.class, () -> userController.getProfileImage(1L));
    }

    @Test
    void testGetProfileImage_IOException() throws UserNotFoundException, IOException {
        when(userService.getUserProfileImage(anyLong())).thenThrow(new IOException("File not found"));

        // Verifying
        assertThrows(IOException.class, () -> userController.getProfileImage(1L));
    }
}
