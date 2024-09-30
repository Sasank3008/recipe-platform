package com.user.userservice.controller;
import com.user.userservice.dto.*;
import com.nimbusds.jose.util.Pair;
import com.user.userservice.constants.ControllerConstants;
import com.user.userservice.feignclient.RecipeServiceClient;
import com.user.userservice.dto.ApiResponse;
import com.user.userservice.dto.UserLoginDTO;
import com.user.userservice.dto.UserRegistrationDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.dto.CountryListDTO;
import com.user.userservice.dto.FileResponse;
import com.user.userservice.dto.PasswordDTO;
import com.user.userservice.dto.UserUpdateDTO;
import com.user.userservice.dto.UpdateRecipeDTO;
import com.user.userservice.dto.UserEmailDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import com.user.userservice.exception.InvalidInputException;
import com.user.userservice.exception.UserAlreadyExistsException;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.exception.IdNotFoundException;
import com.user.userservice.exception.InvalidPasswordException;
import com.user.userservice.service.TokenService;
import com.user.userservice.service.UserService;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RecipeServiceClient recipeServiceClient;
    @Value("${project.image}")
    private String path;

    @PostMapping("login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO userEntity) throws IncorrectPasswordException {
        Pair<String, User> response=userService.login(userEntity);
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .token(response.getLeft())
                .userId(response.getRight().getId())
                .role(response.getRight().getRole())
                .message("User Login Successfully")
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping(value = "register", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> register(@ModelAttribute @Valid UserRegistrationDTO userRegistrationDTO) throws IOException, UserAlreadyExistsException, InvalidInputException {
        return userService.register(userRegistrationDTO);
    }

    @GetMapping("/countries")
    public ResponseEntity<CountryListDTO> fetchAllCountries() {
        return userService.fetchAllCountries();
    }

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
    @GetMapping("/emails")
    public ResponseEntity<?> getAllEmails() {
        try {
            List<UserEmailDTO> emails = userService.getAllUserEmails();
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch user emails");
        }
    }
    @PutMapping(value="/recipe/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateRecipe(@ModelAttribute UpdateRecipeDTO recipeDTO) throws IdNotFoundException, IOException {
        try {
            recipeServiceClient.updateRecipe(recipeDTO);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .response("Recipe updated successfully")
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (FeignException.FeignClientException e) {
            String errorMessage = Optional.ofNullable(e.contentUTF8()).orElse("An error occurred");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.builder()
                            .response(errorMessage)
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        }
    }

    @GetMapping("/comments/{recipeId}")
    public ResponseEntity<AllCommentsDTO> getAllComments(@PathVariable Long recipeId) throws UserNotFoundException {
        List<ReviewRating> comments = recipeServiceClient.getAllComments(recipeId).getBody().getReviews();
        comments = userService.mapEmailAndImage(comments);
        AllCommentsDTO allCommentsDTO = AllCommentsDTO.builder()
                .reviews(comments)
                .build();
        return new ResponseEntity<>(allCommentsDTO, HttpStatus.OK);
    }
}
