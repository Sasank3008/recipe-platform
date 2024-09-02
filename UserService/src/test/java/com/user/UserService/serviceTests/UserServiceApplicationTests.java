package com.user.UserService.serviceTests;

import com.user.UserService.Handler.InvalidPasswordException;
import com.user.UserService.Handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.UserEntity;
import com.user.UserService.repository.UserRepository;
import com.user.UserService.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceApplicationTests {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private MultipartFile file;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createUser_ShouldSaveUser() {
		UserDTO userDTO = new UserDTO();
		UserEntity userEntity = new UserEntity();

		when(modelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);
		when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

		userService.createUser(userDTO);

		verify(userRepository, times(1)).save(userEntity);
	}

	@Test
	void getUser_ShouldReturnUserDTO() throws UserNotFoundException {
		Long userId = 1L;
		UserEntity userEntity = new UserEntity();
		UserDTO userDTO = new UserDTO();

		when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
		when(modelMapper.map(userEntity, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userService.getUser(userId);

		assertNotNull(result);
	}

	@Test
	void getUser_ShouldThrowUserNotFoundException() {
		Long userId = 1L;

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
	}

	@Test
	void updateUser_ShouldUpdateUser() throws UserNotFoundException {
		Long userId = 1L;
		UserDTO userDTO = new UserDTO();
		UserEntity userEntity = new UserEntity();

		when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

		userService.updateUser(userDTO, userId);

		verify(userRepository, times(1)).save(userEntity);
	}

	@Test
	void updateUserImage_ShouldSaveImage() throws IOException, UserNotFoundException {
		Long userId = 1L;
		UserEntity userEntity = new UserEntity();

		when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
		when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

		userService.updateUserImage(file, userId);

		// Verify the user entity's profile image was updated correctly
		assertArrayEquals(new byte[]{1, 2, 3}, userEntity.getProfileImage());

		// Verify that the save method was called
		verify(userRepository, times(1)).save(userEntity);
	}
}
