package com.user.userservice.service;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.dto.UserLoginDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Pair<String, User> login(UserLoginDTO userLoginDTO) throws IncorrectPasswordException;
}
