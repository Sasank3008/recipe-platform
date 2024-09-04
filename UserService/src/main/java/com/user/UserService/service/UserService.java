package com.user.UserService.service;

import com.nimbusds.jose.util.Pair;
import com.user.UserService.dto.UserLoginDTO;
import com.user.UserService.entity.User;
import com.user.UserService.exception.IncorrectPasswordException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Pair<String, User> login(UserLoginDTO userLoginDTO) throws IncorrectPasswordException;
}
