package com.user.UserService.controller;

import com.user.UserService.dto.UserRegistrationDTO;
import com.user.UserService.entity.ApiResponse;
import com.user.UserService.Handler.InvalidPasswordException;
import com.user.UserService.Handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.UserEntity;
import com.user.UserService.repository.UserRepository;
import com.user.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        logger.info("Saving new user: {}", userRegistrationDTO);
        userService.createUser(userRegistrationDTO);
        logger.info("User registered successfully: {}", userRegistrationDTO);
        ApiResponse response = new ApiResponse();
        response.setResponse("User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        ApiResponse response = new ApiResponse();
        try {
            UserDTO userDTO = userService.getUser(id);
            logger.info("User with ID: {} fetched successfully", id);
            response.setResponse("User fetched successfully");
            response.setData(userDTO);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            logger.error("User with ID: {} not found", id);
            response.setResponse("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        logger.info("Updating user with ID: {}", id);
        ApiResponse response = new ApiResponse();
        try {
            userService.updateUser(userDTO, id);
            UserDTO updatedUser = userService.getUser(id);
            logger.info("User with ID: {} updated successfully", id);
            response.setResponse("User updated successfully");
            response.setData(updatedUser);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            logger.error("User with ID: {} not found", id);
            response.setResponse("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping(value = "/image/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateUserImage(@RequestParam("file") MultipartFile file, @PathVariable Long userId) {
        logger.info("Updating profile image for user ID: {}", userId);
        ApiResponse response = new ApiResponse();
        try {
            userService.updateUserImage(file, userId);
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/user/profile-image/")
                    .path(userId.toString())
                    .toUriString();
            logger.info("Profile image updated for user ID: {}", userId);
            response.setResponse("Profile image updated successfully");
            response.setData(Collections.singletonMap("imageUrl", imageUrl));
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            logger.error("User with ID: {} not found", userId);
            response.setResponse("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IOException e) {
            logger.error("Error updating profile image for user ID: {}", userId, e);
            response.setResponse("Error updating profile image");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<ApiResponse> getProfileImage(@PathVariable Long userId) {
        logger.info("Fetching profile image for user ID: {}", userId);
        ApiResponse response = new ApiResponse();
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            if (userEntity.getProfileImage() != null) {
                logger.info("Profile image fetched for user ID: {}", userId);
                response.setResponse("Profile image fetched successfully");
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(response);  // Adjust this as needed based on how you handle image response
            } else {
                logger.warn("No profile image found for user ID: {}", userId);
                response.setResponse("No profile image found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            logger.error("User with ID: {} not found", userId);
            response.setResponse("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody PasswordDTO passwordDTO, @PathVariable Long id) {
        logger.info("Updating password for user ID: {}", id);
        ApiResponse response = new ApiResponse();
        try {
            userService.updatePassword(passwordDTO, id);
            logger.info("Password updated successfully for user ID: {}", id);
            response.setResponse("Password updated successfully");
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            logger.error("User with ID: {} not found", id);
            response.setResponse("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidPasswordException e) {
            logger.error("Invalid password for user ID: {}", id);
            response.setResponse("Invalid password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
