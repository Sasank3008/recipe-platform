package com.user.userservice.controller;

import com.user.UserService.dto.FileResponse;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserUpdateDTO;
import com.user.UserService.exception.InvalidPasswordException;
import com.user.UserService.exception.UserNotFoundException;
import com.user.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Value("${project.image}")
    private String path;

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, @PathVariable Long id) throws UserNotFoundException {
        userService.updateUser(userUpdateDTO, id);
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping(value = "/image/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> updateUserImage(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws UserNotFoundException, IOException {
        return new ResponseEntity<>(userService.updateUserImage(path, file, userId), HttpStatus.OK);
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordDTO passwordDTO, @PathVariable Long id) throws UserNotFoundException, InvalidPasswordException {
        userService.updatePassword(passwordDTO, id);
        return ResponseEntity.ok().body("Password Updated Successfully");
    }

    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<?> getProfileImage(@PathVariable Long userId) throws UserNotFoundException, IOException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userService.getUserProfileImage(userId));
    }
}
