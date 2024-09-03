package com.user.UserService.service;

import com.user.UserService.Handler.InvalidPasswordException;
import com.user.UserService.Handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.dto.UserRegistrationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    void createUser(UserRegistrationDTO userRegistrationDTO);
    UserDTO getUser(Long id) throws UserNotFoundException;
    void updateUser(UserDTO userDTO,Long id) throws UserNotFoundException;
    void updateUserImage(MultipartFile file, Long userId) throws IOException, UserNotFoundException;
    void updatePassword(PasswordDTO passwordDTO, Long userId) throws UserNotFoundException, InvalidPasswordException;

}
