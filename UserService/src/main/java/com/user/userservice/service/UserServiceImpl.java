package com.user.userservice.service;

import com.nimbusds.jose.util.Pair;
import com.user.userservice.dto.UserLoginDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.IncorrectPasswordException;
import com.user.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;


    @Override
    public Pair<String, User> login(UserLoginDTO userLoginDTO) throws IncorrectPasswordException {
        Authentication authentication;
        User user = userDetailsService.loadUserByUsername(userLoginDTO.getEmail());
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword()))
            throw new IncorrectPasswordException("Provided password is incorrect");
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword()
        );
        authentication = this.authenticationManager.authenticate(authRequest);
        return Pair.of(tokenService.generateToken(authentication), user);
    }
}
