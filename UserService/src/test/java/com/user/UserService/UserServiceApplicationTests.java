package com.user.UserService;

import com.user.UserService.handler.InvalidPasswordException;
import com.user.UserService.handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.User;
import com.user.UserService.repository.CountryRepository;
import com.user.UserService.repository.UserRepository;
import com.user.UserService.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.FileNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}




@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private CountryRepository countryRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void getUser_ShouldReturnUser_WhenUserExists() throws UserNotFoundException {
		User user = new User();
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		UserDTO userDTO = new UserDTO();
		when(modelMapper.map(any(User.class), any())).thenReturn(userDTO);

		UserDTO result = userServiceImpl.getUser(1L);

		assertNotNull(result);
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void getUser_ShouldThrowException_WhenUserDoesNotExist() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUser(1L));
	}


	@Test
	void updatePassword_ShouldUpdatePassword_WhenValidOldPassword() throws UserNotFoundException, InvalidPasswordException {
		User user = new User();
		user.setPassword("encodedPassword");

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");

		PasswordDTO passwordDTO = new PasswordDTO();
		passwordDTO.setOldPassword("oldPassword");
		passwordDTO.setNewPassword("newPassword");
		passwordDTO.setConfirmPassword("newPassword");

		userServiceImpl.updatePassword(passwordDTO, 1L);

		verify(userRepository, times(1)).save(user);
		assertEquals("newEncodedPassword", user.getPassword());
	}

	@Test
	void updatePassword_ShouldThrowException_WhenOldPasswordIsInvalid() {
		User user = new User();
		user.setPassword("encodedPassword");

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

		PasswordDTO passwordDTO = new PasswordDTO();
		passwordDTO.setOldPassword("wrongPassword");
		passwordDTO.setNewPassword("newPassword");
		passwordDTO.setConfirmPassword("newPassword");

		assertThrows(InvalidPasswordException.class, () -> userServiceImpl.updatePassword(passwordDTO, 1L));
	}





	@Test
	void getUserProfileImage_ShouldThrowException_WhenImageDoesNotExist() {
		User user = new User();
		user.setProfileImageUrl("image.jpg");
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		assertThrows(FileNotFoundException.class, () -> userServiceImpl.getUserProfileImage(1L));
	}
}
