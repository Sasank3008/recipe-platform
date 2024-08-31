package com.user.UserService.controller;


import com.user.UserService.entity.User;
import com.user.UserService.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class UserAdminController {


    @Autowired
    private UserProfileService userService;

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody User user) {
        return userService.getUserById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setEnabled(user.getEnabled());
                    existingUser.setCountry(user.getCountry());
                    existingUser.setRegion(user.getRegion());
                    User updatedUser = userService.updateUser(existingUser);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}




