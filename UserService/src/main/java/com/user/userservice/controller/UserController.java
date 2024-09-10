package com.user.userservice.controller;

import com.user.userservice.constants.ControllerConstants;
import com.user.userservice.dto.FileResponse;
import com.user.userservice.dto.PasswordDTO;
import com.user.userservice.dto.UserUpdateDTO;
import com.user.userservice.exception.InvalidPasswordException;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.service.UserService;
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
