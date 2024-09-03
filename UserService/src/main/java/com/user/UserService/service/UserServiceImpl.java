package com.user.UserService.service;

import com.user.UserService.Handler.InvalidPasswordException;
import com.user.UserService.Handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.dto.UserRegistrationDTO;
import com.user.UserService.entity.UserEntity;
import com.user.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserRegistrationDTO userRegistrationDTO) {
        UserEntity userEntity = modelMapper.map(userRegistrationDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userRepository.save(userEntity);
    }


    @Override
    public UserDTO getUser(Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
        userDTO.setProfileImageUrl(userEntity.getProfileImageUrl());
        return userDTO;
    }

    @Override
    public void updateUser(UserDTO userDTO, Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
            userEntity.setFirstName(userDTO.getFirstName());
            userEntity.setLastName(userDTO.getLastName());
            userEntity.setCountry(userDTO.getCountry());
            userEntity.setRegion(userDTO.getRegion());
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
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String oldPassword = passwordDTO.getOldPassword();
        String encodedPassword = userEntity.getPassword();

        if (!passwordEncoder.matches(oldPassword, encodedPassword)) {
            throw new InvalidPasswordException("Invalid Password");
        }

        String newPassword = passwordDTO.getNewPassword();
        String confirmPassword = passwordDTO.getConfirmPassword();

        if (newPassword.equals(confirmPassword)) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
        } else {
            throw new InvalidPasswordException("New password and Confirm password do not match");
        }
    }

}


