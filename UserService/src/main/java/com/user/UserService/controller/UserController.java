package com.user.UserService.controller;


import com.user.UserService.Handler.InvalidPasswordException;
import com.user.UserService.Handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.UserEntity;
import com.user.UserService.repository.UserRepository;
import com.user.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
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

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?>saveUser(@RequestBody UserDTO userDTO){
        userService.createUser(userDTO);
        return new ResponseEntity<>("User Registered Successfully",HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws UserNotFoundException {
       // return new ResponseEntity<>(userService.getUser(id),HttpStatus.OK);
        return  ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) throws UserNotFoundException {

            userService.updateUser(userDTO, id);
            UserDTO updatedUser = userService.getUser(id);
           // return new ResponseEntity<>(updatedUser, HttpStatus.OK);
           return ResponseEntity.ok().body(updatedUser);

    }


    @PutMapping(value = "/image/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws UserNotFoundException, IOException {
        // Update user image in the service
        userService.updateUserImage(file, userId);

        // Generate the URL for the updated profile image
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/profile-image/")
                .path(userId.toString())
                .toUriString();

        // Return the URL in the response as JSON
        return ResponseEntity.ok(Collections.singletonMap("imageUrl", imageUrl));
    }

    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long userId) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userEntity.getProfileImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Adjust as needed
                .body(userEntity.getProfileImage());
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordDTO passwordDTO,@PathVariable Long id) throws UserNotFoundException, InvalidPasswordException {
        userService.updatePassword(passwordDTO,id);
        return ResponseEntity
                .ok()
                .body("Password Updated Successfully");

    }
}
