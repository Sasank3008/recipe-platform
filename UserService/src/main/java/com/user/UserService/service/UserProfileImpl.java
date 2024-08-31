package com.user.UserService.service;

import com.user.UserService.dao.UserRepository;
import com.user.UserService.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserProfileImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }

}
