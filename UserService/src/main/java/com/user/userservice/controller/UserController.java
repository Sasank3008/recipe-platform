package com.user.userservice.controller;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.constants.ControllerConstants;
import com.user.userservice.dto.*;
import com.user.userservice.dto.UserRegistrationDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import com.user.userservice.exception.InvalidInputException;
import com.user.userservice.exception.InvalidPasswordException;
import com.user.userservice.exception.UserAlreadyExistsException;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.service.TokenService;
import com.user.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    @Value("${project.image}")
    private String path;

    @PostMapping("login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO userEntity) throws IncorrectPasswordException {
        Pair<String, User> response=userService.login(userEntity);
        return ResponseEntity.ok(UserResponseDTO.builder().token(response.getLeft()).userId(response.getRight().getId()).role(response.getRight().getRole()).message("User Login Successfully").time(LocalDateTime.now()).build());
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@ModelAttribute @Valid UserRegistrationDTO userRegistrationDTO) throws IOException, UserAlreadyExistsException, InvalidInputException {
        return userService.register(userRegistrationDTO);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> fetchAllCountries() {
        return userService.fetchAllCountries();
    }

    @GetMapping(ControllerConstants.USER_ID_PATH)
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping(ControllerConstants.UPDATE_PATH + "{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, @PathVariable Long id) throws UserNotFoundException {
        userService.updateUser(userUpdateDTO, id);
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping(value = ControllerConstants.IMAGE_PATH + "{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> updateUserImage(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws UserNotFoundException, IOException {
        return new ResponseEntity<>(userService.updateUserImage(path, file, userId), HttpStatus.OK);
    }

    @PutMapping(ControllerConstants.PASSWORD_PATH + "{id}")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordDTO passwordDTO, @PathVariable Long id) throws UserNotFoundException, InvalidPasswordException {
        userService.updatePassword(passwordDTO, id);
        return ResponseEntity.ok().body("Password Updated Successfully");
    }

    @GetMapping(ControllerConstants.PROFILE_IMAGE_PATH + "{userId}")
    public ResponseEntity<?> getProfileImage(@PathVariable Long userId) throws UserNotFoundException, IOException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userService.getUserProfileImage(userId));
    }

}
