package com.user.UserService.controller;

import com.user.UserService.handler.UserIdNotFoundException;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Details")
public class UserProfileController {


    @Autowired
    private UserProfileService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) throws UserIdNotFoundException {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PostMapping("/add")
//    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
//        try {
//            UserDTO savedUserDTO = userService.createUser(userDTO);
//            return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) throws UserIdNotFoundException {

        UserDTO updatedUserDTO = userService.updateUser(id, userDTO);
     //   UserDTO l=userService.getUserById(id).get();
        if (updatedUserDTO != null) {
            return ResponseEntity.ok(updatedUserDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

