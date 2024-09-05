package com.user.userservice.controller;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.dto.UserLoginDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import com.user.userservice.service.TokenService;
import com.user.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO userEntity) throws IncorrectPasswordException {
        Pair<String, User> response=userService.login(userEntity);
        return ResponseEntity.ok(UserResponseDTO.builder().token(response.getLeft()).userId(response.getRight().getId()).role(response.getRight().getRole()).message("User Login Successfully").time(LocalDateTime.now()).build());
    }
}
