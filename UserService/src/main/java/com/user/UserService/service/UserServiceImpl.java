package com.user.UserService.service;

import com.user.UserService.handler.InvalidPasswordException;
import com.user.UserService.handler.UserNotFoundException;
import com.user.UserService.dto.PasswordDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.Country;
import com.user.UserService.entity.User;
import com.user.UserService.repository.CountryRepository;
import com.user.UserService.repository.UserRepository;
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
    public UserDTO getUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setProfileImageUrl(user.getProfileImageUrl()); // Set the profile image URL
        return userDTO;
    }


    @Override
    public void updateUser(UserDTO userDTO, Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        Country country = countryRepository.findByName(userDTO.getCountry().getName()).get();
        user.setCountry(country);
        user.setRegion(userDTO.getRegion());

        userRepository.save(user);
    }


    @Override
    public String updateUserImage(String path, MultipartFile file, Long userId) throws IOException, UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        String originalFilename = file.getOriginalFilename();
        String randomID = UUID.randomUUID().toString();
        String newFilename = randomID.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator + newFilename;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        user.setProfileImageUrl(newFilename);
        userRepository.save(user);
        return newFilename;
    }


    @Override
    public void updatePassword(PasswordDTO passwordDTO, Long userId) throws UserNotFoundException, InvalidPasswordException {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found"));

        String oldPassword =  passwordDTO.getOldPassword();
        String encodedPassword = user.getPassword();
        System.out.println(oldPassword);
        System.out.println(encodedPassword);

        String newPassword = passwordDTO.getNewPassword();
        String confirmPassword = passwordDTO.getConfirmPassword();

        if(!passwordEncoder.matches(oldPassword,encodedPassword)){
            throw new InvalidPasswordException("Invalid Password");
        }
        if (newPassword.equals(confirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        else {
            throw new InvalidPasswordException("New password and Confirm password do not matched");
        }


    }

    @Override
    public byte[] getUserProfileImage(Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String imageUrl = "images/"+ user.getProfileImageUrl();


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


