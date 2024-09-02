package com.user.UserService.service;

import com.user.UserService.Handler.InvalidPasswordException;
import com.user.UserService.Handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.UserEntity;
import com.user.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private String uploadDir; // Directory for storing uploaded files

    @Override
    public void createUser(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public UserDTO getUser(Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
        userDTO.setProfileImageUrl(userEntity.getProfileImageUrl()); // Set the profile image URL
        return userDTO;
    }


    @Override
    public void updateUser(UserDTO userDTO, Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

            // Copy properties from DTO to entity except for the id
            userEntity.setFirstName(userDTO.getFirstName());
            userEntity.setLastName(userDTO.getLastName());
            userEntity.setCountry(userDTO.getCountry());
            userEntity.setRegion(userDTO.getRegion());
//            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
//                userEntity.setPassword(userDTO.getPassword());
//            }


            userRepository.save(userEntity);

    }


    @Override
    public void updateUserImage(MultipartFile file, Long userId) throws IOException, UserNotFoundException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (file != null && !file.isEmpty()) {
            byte[] bytes = file.getBytes();
            userEntity.setProfileImage(bytes);
        }

        userRepository.save(userEntity);
    }

    @Override
    public void updatePassword(PasswordDTO passwordDTO, Long userId) throws UserNotFoundException, InvalidPasswordException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found"));

        String oldPassword =  passwordDTO.getOldPassword();
        String encodedPassword = userEntity.getPassword();
        System.out.println(oldPassword);
        System.out.println(encodedPassword);

        String newPassword = passwordDTO.getNewPassword();
        String confirmPassword = passwordDTO.getConfirmPassword();

        if(!passwordEncoder.matches(oldPassword,encodedPassword)){
            throw new InvalidPasswordException("Invalid Password");
        }
        if (newPassword.equals(confirmPassword)) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
        }
        else {
            throw new InvalidPasswordException("New password and Confirm password do not matched");
        }


    }
}


