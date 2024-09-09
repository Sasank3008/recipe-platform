package com.user.UserService.service;

import com.user.UserService.dto.FileResponse;
import com.user.UserService.dto.UserDisplayDTO;
import com.user.UserService.exception.InvalidPasswordException;
import com.user.UserService.exception.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    UserDisplayDTO getUser(Long id) throws UserNotFoundException;

    void updateUser(UserUpdateDTO userUpdateDTO, Long id) throws UserNotFoundException;

    FileResponse updateUserImage(String path, MultipartFile file, Long userId) throws IOException, UserNotFoundException;

    void updatePassword(PasswordDTO passwordDTO, Long userId) throws UserNotFoundException, InvalidPasswordException;

    byte[] getUserProfileImage(Long userId) throws UserNotFoundException, IOException;

    String getUserProfileImageUrl(Long userId) throws UserNotFoundException, IOException;
}
