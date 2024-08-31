package com.user.UserService.service;

import com.user.UserService.entity.User;

import java.util.Optional;

public interface UserProfileService {
    public Optional<User> getUserById(Long id);
    public User updateUser(User user);


    public User createUser(User user);



}
