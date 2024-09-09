package com.user.userservice.service;

import com.user.userservice.dto.FileResponse;
import com.user.userservice.dto.UserDisplayDTO;
import com.user.userservice.exception.InvalidPasswordException;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.dto.PasswordDTO;
import com.user.userservice.dto.UserUpdateDTO;
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
