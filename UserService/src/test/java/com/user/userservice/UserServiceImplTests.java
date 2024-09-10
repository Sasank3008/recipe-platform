package com.user.userservice;

import com.user.userservice.dto.PasswordDTO;
import com.user.userservice.dto.UserDisplayDTO;
import com.user.userservice.dto.UserUpdateDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.InvalidPasswordException;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.repository.UserRepository;
import com.user.userservice.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDisplayDTO userDisplayDTO;
    private UserUpdateDTO userUpdateDTO;
    private PasswordDTO passwordDTO;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("encodedPassword");
        user.setProfileImageUrl("image.jpg");

        userDisplayDTO = new UserDisplayDTO();
        userDisplayDTO.setFirstName("John");
        userDisplayDTO.setLastName("Doe");

        userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setFirstName("Jane");
        userUpdateDTO.setLastName("Doe");

        passwordDTO = new PasswordDTO();
        passwordDTO.setOldPassword("oldPassword");
        passwordDTO.setNewPassword("newPassword");
        passwordDTO.setConfirmPassword("newPassword");
    }

    @Test
    public void testGetUser_Success() throws UserNotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(modelMapper.map(any(User.class), any(Class.class))).thenReturn(userDisplayDTO);

        UserDisplayDTO result = userService.getUser(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetUser_UserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
        verify(userRepository, times(1)).findById(anyLong());
    }


    @Test
    public void testUpdateUser_UserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userUpdateDTO, 1L));
        verify(userRepository, times(1)).findById(anyLong());
    }


    @Test
    public void testUpdateUserImage_UserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUserImage("path/to/images", multipartFile, 1L));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdatePassword_Success() throws UserNotFoundException, InvalidPasswordException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        userService.updatePassword(passwordDTO, 1L);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdatePassword_InvalidPasswordException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.updatePassword(passwordDTO, 1L));
    }



    @Test
    public void testGetUserProfileImage_UserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserProfileImage(1L));
    }

    @Test
    public void testGetUserProfileImage_FileNotFoundException() throws UserNotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(FileNotFoundException.class, () -> userService.getUserProfileImage(1L));
    }


    @Test
    public void testGetUserProfileImageUrl_UserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserProfileImageUrl(1L));
    }

    @Test
    public void testGetUserProfileImageUrl_FileNotFoundException() throws UserNotFoundException {
        user.setProfileImageUrl(null);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(FileNotFoundException.class, () -> userService.getUserProfileImageUrl(1L));
    }
}
