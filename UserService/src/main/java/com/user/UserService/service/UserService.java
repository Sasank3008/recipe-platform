package com.user.UserService.service;

import com.user.UserService.handler.InvalidPasswordException;
import com.user.UserService.handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface UserService {


    UserDTO getUser(Long id) throws UserNotFoundException;
    void updateUser(UserDTO userDTO,Long id) throws UserNotFoundException;
    String updateUserImage(String path, MultipartFile file, Long userId) throws IOException, UserNotFoundException;
    void updatePassword(PasswordDTO passwordDTO, Long userId) throws UserNotFoundException, InvalidPasswordException;
    byte[] getUserProfileImage(Long userId) throws UserNotFoundException, IOException;
    String getUserProfileImageUrl(Long userId) throws UserNotFoundException, FileNotFoundException;

}
