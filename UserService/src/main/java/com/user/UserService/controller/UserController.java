package com.user.UserService.controller;


import com.user.UserService.handler.InvalidPasswordException;
import com.user.UserService.handler.UserNotFoundException;
import com.user.UserService.dto.FileResponse;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.repository.UserRepository;
import com.user.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${project.image}")
    private String path;


    @PostMapping
    public ResponseEntity<?>saveUser(@RequestBody UserDTO userDTO){
        userService.createUser(userDTO);
        return new ResponseEntity<>("User Registered Successfully",HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws UserNotFoundException {

        return  ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) throws UserNotFoundException{

        userService.updateUser(userDTO, id);
        UserDTO updatedUser = userService.getUser(id);
        return ResponseEntity.ok().body(updatedUser);

    }


    @PutMapping(value = "/image/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> updateUserImage(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws UserNotFoundException, IOException {
        String name =userService.updateUserImage(path,file, userId);
        FileResponse response = FileResponse.builder()
                .fileName(name)
                .message("Image updated successfully")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordDTO passwordDTO,@PathVariable Long id) throws UserNotFoundException, InvalidPasswordException {
        userService.updatePassword(passwordDTO,id);
        return ResponseEntity
                .ok()
                .body("Password Updated Successfully");

    }

    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<?> getProfileImage(@PathVariable Long userId) throws UserNotFoundException, IOException {
        byte[] imageBytes = userService.getUserProfileImage(userId);
        Path imagePath = Paths.get(userService.getUserProfileImageUrl(userId));
        String contentType = Files.probeContentType(imagePath);
        MediaType mediaType = MediaType.parseMediaType(contentType);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageBytes);
    }
}
