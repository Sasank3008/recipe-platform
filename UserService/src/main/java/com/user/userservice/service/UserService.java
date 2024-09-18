package com.user.userservice.service;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.dto.*;
import com.user.userservice.entity.Country;
import com.user.userservice.entity.User;
import com.user.userservice.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {
    Pair<String, User> login(UserLoginDTO userLoginDTO) throws IncorrectPasswordException;
    UserDisplayDTO getUser(Long id) throws UserNotFoundException;
    void updateUser(UserUpdateDTO userUpdateDTO, Long id) throws UserNotFoundException;
    FileResponse updateUserImage(String path, MultipartFile file, Long userId) throws IOException, UserNotFoundException;
    void updatePassword(PasswordDTO passwordDTO, Long userId) throws UserNotFoundException, InvalidPasswordException;
    byte[] getUserProfileImage(Long userId) throws UserNotFoundException, IOException;
    String getUserProfileImageUrl(Long userId) throws UserNotFoundException, IOException;

    ResponseEntity<ApiResponse> register(UserRegistrationDTO userRegistrationDTO) throws IOException, UserAlreadyExistsException, InvalidInputException;

    String uploadImage(String path, MultipartFile file) throws IOException, InvalidInputException;

    Country fetchCountryById(String country) throws InvalidInputException;

    ResponseEntity<CountryListDTO> fetchAllCountries();

    User mapUserRegistrationDTOtoUser(UserRegistrationDTO userRegistrationDTO) throws InvalidInputException, IOException;
}
