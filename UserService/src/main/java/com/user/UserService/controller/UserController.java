package com.user.UserService.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        logger.info("Saving new user: {}", userDTO);
        userService.createUser(userDTO);
        logger.info("User registered successfully: {}", userDTO);
        return new ResponseEntity<>("User Registered Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws UserNotFoundException {
        logger.info("Fetching user with ID: {}", id);
        ResponseEntity<?> response = ResponseEntity.ok().body(userService.getUser(id));
        logger.info("User with ID: {} fetched successfully", id);
        return response;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) throws UserNotFoundException {
        logger.info("Updating user with ID: {}", id);
        userService.updateUser(userDTO, id);
        UserDTO updatedUser = userService.getUser(id);
        logger.info("User with ID: {} updated successfully", id);
        return ResponseEntity.ok().body(updatedUser);
    }

    @PutMapping(value = "/image/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws UserNotFoundException, IOException {
        logger.info("Updating profile image for user ID: {}", userId);
        userService.updateUserImage(file, userId);
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/profile-image/")
                .path(userId.toString())
                .toUriString();
        logger.info("Profile image updated for user ID: {}", userId);
        return ResponseEntity.ok(Collections.singletonMap("imageUrl", imageUrl));
    }

    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long userId) throws UserNotFoundException {
        logger.info("Fetching profile image for user ID: {}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userEntity.getProfileImage() == null) {
            logger.warn("No profile image found for user ID: {}", userId);
            return ResponseEntity.notFound().build();
        }

        logger.info("Profile image fetched for user ID: {}", userId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userEntity.getProfileImage());
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordDTO passwordDTO, @PathVariable Long id) throws UserNotFoundException, InvalidPasswordException {
        logger.info("Updating password for user ID: {}", id);
        userService.updatePassword(passwordDTO, id);
        logger.info("Password updated successfully for user ID: {}", id);
        return ResponseEntity.ok().body("Password Updated Successfully");
    }
}
