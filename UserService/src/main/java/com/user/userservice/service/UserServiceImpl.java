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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            throw new IncorrectPasswordException("Provided password is incorrect");
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword()
        );
        authentication = this.authenticationManager.authenticate(authRequest);
        return Pair.of(tokenService.generateToken(authentication), user);
    }

    @Override
    public ResponseEntity<ApiResponse> register(UserRegistrationDTO userRegistrationDTO) throws IOException, UserAlreadyExistsException, InvalidInputException {
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new UserAlreadyExistsException("This Email already exists : " + userRegistrationDTO.getEmail());
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

        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueIdentifier = UUID.randomUUID().toString();
        String newFileName = uniqueIdentifier + fileExtension;

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
            throw new InvalidInputException("No country found with ID: " + country);
        }
    }

    @Override
    public ResponseEntity<List<Country>> fetchAllCountries() {
        return ResponseEntity.ok(countryRepository.findAll());
    }
}
