package com.user.userservice.controller;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.dto.ApiResponse;
import com.user.userservice.dto.UserLoginDTO;
import com.user.userservice.dto.UserRegistrationDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import com.user.userservice.exception.InvalidInputException;
import com.user.userservice.exception.UserAlreadyExistsException;
import com.user.userservice.service.TokenService;
import com.user.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO userEntity) throws IncorrectPasswordException {
        Pair<String, User> response=userService.login(userEntity);
        return ResponseEntity.ok(UserResponseDTO.builder().token(response.getLeft()).userId(response.getRight().getId()).role(response.getRight().getRole()).message("User Login Successfully").time(LocalDateTime.now()).build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse> register(@ModelAttribute @Valid UserRegistrationDTO userRegistrationDTO) throws IOException, UserAlreadyExistsException, InvalidInputException {
        return userService.register(userRegistrationDTO);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> fetchAllCountries() {
        return userService.fetchAllCountries();
    }

}
