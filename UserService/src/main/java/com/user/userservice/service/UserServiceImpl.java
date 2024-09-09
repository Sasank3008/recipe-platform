package com.user.userservice.service;

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
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDisplayDTO userDisplayDTO = modelMapper.map(user, UserDisplayDTO.class);
        userDisplayDTO.setProfileImageUrl(user.getProfileImageUrl());
        return userDisplayDTO;
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO, Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

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
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("File name must not be null");
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!fileExtension.equals(".png") && !fileExtension.equals(".jpg") &&
                !fileExtension.equals(".jpeg") && !fileExtension.equals(".svg")) {
            throw new IllegalArgumentException("Invalid file type. Only PNG, JPG, JPEG, and SVG are allowed.");
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
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String oldPassword = passwordDTO.getOldPassword();
        String encodedPassword = user.getPassword();

        String newPassword = passwordDTO.getNewPassword();
        String confirmPassword = passwordDTO.getConfirmPassword();

        if (!passwordEncoder.matches(oldPassword, encodedPassword)) {
            throw new InvalidPasswordException("Invalid Password");
        }
        if (newPassword.equals(confirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new InvalidPasswordException("New password and Confirm password do not match");
        }
    }

    @Override
    public byte[] getUserProfileImage(Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String imageUrl = path + File.separator + user.getProfileImageUrl();

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new FileNotFoundException("Image URL not found for user ID: " + userId);
        }
        Path imagePath = Paths.get(imageUrl);

        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("Image file not found at path: " + imageUrl);
        }
        return Files.readAllBytes(imagePath);
    }

    @Override
    public String getUserProfileImageUrl(Long userId) throws UserNotFoundException, FileNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String imageUrl = user.getProfileImageUrl();

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new FileNotFoundException("Image URL not found for user ID: " + userId);
        }

        return imageUrl;
    }
}
