package com.user.userservice.service;
import com.user.userservice.constants.ErrorConstants;
import com.user.userservice.dto.FileResponse;
import com.user.userservice.dto.UserDisplayDTO;
import com.user.userservice.exception.InvalidPasswordException;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.dto.PasswordDTO;
import com.user.userservice.dto.UserUpdateDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.entity.User;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CountryRepository countryRepository;

    @Value("${project.image}")
    private String path;

    @Override
    public UserDisplayDTO getUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));
        UserDisplayDTO userDisplayDTO = modelMapper.map(user, UserDisplayDTO.class);
        userDisplayDTO.setProfileImageUrl(user.getProfileImageUrl());
        return userDisplayDTO;
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO, Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        Country country = countryRepository.findById(userUpdateDTO.getCountry().getId()).get();
        user.setCountry(country);
        user.setRegion(userUpdateDTO.getRegion());

        userRepository.save(user);
    }

    @Override
    public FileResponse updateUserImage(String path, MultipartFile file, Long userId) throws IOException, UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        if (file.isEmpty()) {
            throw new IllegalArgumentException(ErrorConstants.FILE_MUST_NOT_BE_EMPTY);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException(ErrorConstants.FILE_NAME_MUST_NOT_BE_NULL);
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!fileExtension.equals(".png") && !fileExtension.equals(".jpg") &&
                !fileExtension.equals(".jpeg") && !fileExtension.equals(".svg")) {
            throw new IllegalArgumentException(ErrorConstants.INVALID_FILE_TYPE);
        }

        String randomID = UUID.randomUUID().toString();
        String newFilename = randomID.concat(fileExtension);
        String filePath = path + File.separator + newFilename;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));

        user.setProfileImageUrl(newFilename);
        userRepository.save(user);

        return FileResponse.builder()
                .fileName(newFilename)
                .message("Image updated successfully")
                .build();
    }

    @Override
    public void updatePassword(PasswordDTO passwordDTO, Long userId) throws UserNotFoundException, InvalidPasswordException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        String oldPassword = passwordDTO.getOldPassword();
        String encodedPassword = user.getPassword();

        String newPassword = passwordDTO.getNewPassword();
        String confirmPassword = passwordDTO.getConfirmPassword();

        if (!passwordEncoder.matches(oldPassword, encodedPassword)) {
            throw new InvalidPasswordException(ErrorConstants.INVALID_PASSWORD);
        }
        if (newPassword.equals(confirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new InvalidPasswordException(ErrorConstants.PASSWORDS_DO_NOT_MATCH);
        }
    }

    @Override
    public byte[] getUserProfileImage(Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        String imageUrl = path + File.separator + user.getProfileImageUrl();

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new FileNotFoundException(ErrorConstants.IMAGE_URL_NOT_FOUND + userId);
        }
        Path imagePath = Paths.get(imageUrl);

        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException(ErrorConstants.IMAGE_FILE_NOT_FOUND + imageUrl);
        }
        return Files.readAllBytes(imagePath);
    }

    @Override
    public String getUserProfileImageUrl(Long userId) throws UserNotFoundException, FileNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND));

        String imageUrl = user.getProfileImageUrl();

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new FileNotFoundException(ErrorConstants.IMAGE_URL_NOT_FOUND + userId);
        }

        return imageUrl;
    }
}
