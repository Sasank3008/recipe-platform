package com.user.UserService;

import com.user.UserService.controller.UserController;
import com.user.UserService.dto.FileResponse;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_ShouldReturnCreatedStatus_WhenUserIsSavedSuccessfully() {
        UserDTO userDTO = new UserDTO();
        doNothing().when(userService).createUser(any(UserDTO.class));

        ResponseEntity<?> response = userController.saveUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User Registered Successfully", response.getBody());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(userService.getUser(anyLong())).thenReturn(userDTO);

        ResponseEntity<?> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }


    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserIsUpdated() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(userService.getUser(anyLong())).thenReturn(userDTO);

        ResponseEntity<?> response = userController.updateUser(userDTO, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }


    @Test
    void updatePassword_ShouldReturnOkStatus_WhenPasswordIsUpdatedSuccessfully() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO();
        doNothing().when(userService).updatePassword(any(PasswordDTO.class), anyLong());

        ResponseEntity<?> response = userController.updatePassword(passwordDTO, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password Updated Successfully", response.getBody());
    }

    @Test
    void getProfileImage_ShouldReturnImageBytes_WhenImageExists() throws Exception {
        byte[] imageBytes = new byte[]{1, 2, 3};
        when(userService.getUserProfileImage(anyLong())).thenReturn(imageBytes);
        when(userService.getUserProfileImageUrl(anyLong())).thenReturn("image.jpg");

        ResponseEntity<?> response = userController.getProfileImage(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(imageBytes, (byte[]) response.getBody());
    }
}
