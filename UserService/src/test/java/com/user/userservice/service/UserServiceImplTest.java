package com.user.userservice.service;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.dto.ApiResponse;
import com.user.userservice.dto.UserLoginDTO;
import com.user.userservice.dto.UserRegistrationDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import com.user.userservice.exception.InvalidInputException;
import com.user.userservice.exception.UserAlreadyExistsException;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final String token = "mocked-token";
    @TempDir
    Path temporaryFolder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private ModelMapper modelMapper;
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
        ReflectionTestUtils.setField(userService, "path", "images/");
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

    @Test
    void testRegisterNewUser() throws IOException, UserAlreadyExistsException, InvalidInputException {

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(countryRepository.findById(anyLong())).thenReturn(Optional.of(new Country()));
        when(modelMapper.map(any(UserRegistrationDTO.class), eq(User.class))).thenReturn(new User());

        ResponseEntity<ApiResponse> response = userService.register(dummyUserRegistrationDTO());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Registration Successful", response.getBody().getResponse());
    }

    public UserRegistrationDTO dummyUserRegistrationDTO() {
        UserRegistrationDTO dto = new UserRegistrationDTO();

        dto.setEmail("newuser@example.com");
        dto.setCountry("1");
        dto.setFile(new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes()));

        return dto;
    }

    @Test
    void testUploadImage() throws IOException, InvalidInputException {
        MultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "Test content".getBytes());

        String path = "uploads";
        String result = userService.uploadImage(path, file);

        assertNotNull(result, "The result should not be null");
        assertTrue(result.contains(".jpg"), "The result should contain the file extension .jpg");
    }

    @Test
    void fetchCountryById() throws InvalidInputException {
        Long countryId = 1L;
        Country expectedCountry = dummyCountry();

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(expectedCountry));
        Country result = userService.fetchCountryById(String.valueOf(countryId));

        assertNotNull(result, "The country should not be null");
        assertEquals(expectedCountry.getId(), result.getId(), "The country IDs should match");
        assertEquals(expectedCountry.getName(), result.getName(), "The country names should match");

        verify(countryRepository).findById(countryId);
    }

    public Country dummyCountry() {
        Country expectedCountry = new Country();

        expectedCountry.setId(1L);
        expectedCountry.setName("Test Country");

        return expectedCountry;
    }

    @Test
    void fetchAllCountries() {
        when(countryRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Country>> response = userService.fetchAllCountries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserRegistrationDTO dto = new UserRegistrationDTO();

        dto.setEmail("existing@example.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(dto), "This Email already exists : " + dto.getEmail());
        verify(userRepository).existsByEmail(dto.getEmail());
    }

    @Test
    void testUploadImageCreatesNewFolder() throws IOException, InvalidInputException {
        String directoryName = "newFolder";
        Path newFolderPath = temporaryFolder.resolve(directoryName);
        String path = newFolderPath.toString();
        MultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                "image/jpeg",
                "Dummy image content".getBytes()
        );

        String result = userService.uploadImage(path, file);

        assertTrue(Files.exists(newFolderPath), "Directory should be created by the method");
        assertNotNull(result, "The result should not be null");
        assertTrue(result.contains(".jpg"), "The result should contain the file extension .jpg");
    }
}