package com.user.userservice.service;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.constants.ErrorConstants;
import com.user.userservice.dto.*;
import com.user.userservice.dto.UserRegistrationDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import com.user.userservice.exception.InvalidInputException;
import com.user.userservice.exception.InvalidPasswordException;
import com.user.userservice.exception.UserAlreadyExistsException;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.user.userservice.dto.FileResponse;
import com.user.userservice.dto.UserDisplayDTO;
import com.user.userservice.dto.PasswordDTO;
import com.user.userservice.dto.UserUpdateDTO;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    @Value("${project.image}")
    String path;

    @Override
    public Pair<String, User> login(UserLoginDTO userLoginDTO) throws IncorrectPasswordException {
        Authentication authentication;
        User user = userDetailsService.loadUserByUsername(userLoginDTO.getEmail());
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword()))
            throw new IncorrectPasswordException(ErrorConstants.INVALID_PASSWORD);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword()
        );
        authentication = this.authenticationManager.authenticate(authRequest);
        return Pair.of(tokenService.generateToken(authentication), user);
    }

    @Override
    public ResponseEntity<ApiResponse> register(UserRegistrationDTO userRegistrationDTO) throws IOException, InvalidInputException {
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new InvalidInputException(ErrorConstants.EMAIL_EXISTS + userRegistrationDTO.getEmail());
        }

        userRepository.save(mapUserRegistrationDTOtoUser(userRegistrationDTO));
        ApiResponse response = ApiResponse.builder()
                .response("Registration Successful")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public User mapUserRegistrationDTOtoUser(UserRegistrationDTO userRegistrationDTO) throws InvalidInputException, IOException {
        User user = modelMapper.map(userRegistrationDTO, User.class);

        user.setCountry(fetchCountryById(userRegistrationDTO.getCountry()));
        user.setDate(LocalDate.now());
        user.setEnabled(true);
        user.setImage(uploadImage(path, userRegistrationDTO.getFile()));
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setRole("USER");

        return user;
    }

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException, NullPointerException, InvalidInputException {
        if (file == null || file.isEmpty()) {
            throw new InvalidInputException("File must not be null or empty.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new InvalidInputException("File must have a valid name.");
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
        if (!allowedExtensions.contains(fileExtension)) {
            throw new InvalidInputException(ErrorConstants.INVALID_FILE_TYPE);
        }

        String uniqueIdentifier = UUID.randomUUID().toString();
        String newFileName = uniqueIdentifier + fileExtension;

        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Files.copy(file.getInputStream(), Paths.get(path, newFileName));
        return newFileName;
    }

    @Override
    public Country fetchCountryById(String country) throws InvalidInputException {
        long id = Long.parseLong(country);
        Optional<Country> optionalCountry = countryRepository.findById(id);

        if (optionalCountry.isPresent()) {
            return optionalCountry.get();
        } else {
            throw new InvalidInputException(ErrorConstants.COUNTRY_NOT_FOUND + country);
        }
    }

    @Override
    public ResponseEntity<CountryListDTO> fetchAllCountries() {
        List<Country> countryList = countryRepository.findAll();
        CountryListDTO countryListDTO = CountryListDTO.builder()
                .countryList(countryList)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.name())
                .build();

        return ResponseEntity.ok(countryListDTO);
    }

    @Override
    public UserDisplayDTO getUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));
        UserDisplayDTO userDisplayDTO = modelMapper.map(user, UserDisplayDTO.class);
        userDisplayDTO.setProfileImageUrl(user.getImage());
        return userDisplayDTO;
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO, Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        Country country = countryRepository.findById(userUpdateDTO.getCountry().getId()).get();
        user.setCountry(country);
        user.setRegion(userUpdateDTO.getRegion());

        userRepository.save(user);
    }

    @Override
    public FileResponse updateUserImage(String path, MultipartFile file, Long userId) throws IOException, UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        if (file.isEmpty()) {
            throw new IllegalArgumentException(ErrorConstants.FILE_MUST_NOT_BE_EMPTY);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException(ErrorConstants.FILE_NAME_MUST_NOT_BE_NULL);
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!fileExtension.equals(".png") && !fileExtension.equals(".jpg") &&
                !fileExtension.equals(".jpeg") && !fileExtension.equals(".svg")) {
            throw new IllegalArgumentException(ErrorConstants.INVALID_FILE_TYPE);
        }

        String randomID = UUID.randomUUID().toString();
        String newFilename = randomID.concat(fileExtension);
        String filePath = path + File.separator + newFilename;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));

        user.setImage(newFilename);
        userRepository.save(user);

        return FileResponse.builder()
                .fileName(newFilename)
                .message("Image updated successfully")
                .build();
    }

    @Override
    public void updatePassword(PasswordDTO passwordDTO, Long userId) throws UserNotFoundException, InvalidPasswordException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        String oldPassword = passwordDTO.getOldPassword();
        String encodedPassword = user.getPassword();

        String newPassword = passwordDTO.getNewPassword();
        String confirmPassword = passwordDTO.getConfirmPassword();

        if (!passwordEncoder.matches(oldPassword, encodedPassword)) {
            throw new InvalidPasswordException(ErrorConstants.INVALID_PASSWORD);
        }
        if (newPassword.equals(confirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new InvalidPasswordException(ErrorConstants.PASSWORDS_DO_NOT_MATCH);
        }
    }

    @Override
    public byte[] getUserProfileImage(Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        String imageUrl = path + File.separator + user.getImage();

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new FileNotFoundException(ErrorConstants.IMAGE_URL_NOT_FOUND + userId);
        }
        Path imagePath = Paths.get(imageUrl);

        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException(ErrorConstants.IMAGE_FILE_NOT_FOUND + imageUrl);
        }
        return Files.readAllBytes(imagePath);
    }

    @Override
    public String getUserProfileImageUrl(Long userId) throws UserNotFoundException, FileNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        String imageUrl = user.getImage();

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new FileNotFoundException(ErrorConstants.IMAGE_URL_NOT_FOUND + userId);
        }

        return imageUrl;
    }
}
