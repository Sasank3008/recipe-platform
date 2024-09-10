package com.user.userservice.service;

import com.user.userservice.dto.AdminUserDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.entity.User;
import com.user.userservice.exception.UserIdNotFoundException;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    @Transactional
    public AdminUserDTO updateUser(Long id, AdminUserDTO userDTO) throws UserIdNotFoundException{
        return userRepository.findById(id).map(existingUser -> {
           existingUser= modelMapper.map(userDTO, User.class);
           existingUser.setId(id);
            System.out.println(existingUser);
            Country country = countryRepository.findById(userDTO.getCountry().getId()).get();
            existingUser.setCountry(country);

        return convertToDTO(userRepository.save(existingUser));
        }).orElseThrow(()->new UserIdNotFoundException("User ID not found with id "+id));
//        User existing=userRepository.findById(id).get();
//        modelMapper.map(userDTO,existing);
//
//        existing.setCountry(countryRepository.findById(userDTO.getCountry().getId()).get());
//        userRepository.flush(existing);
//
//        return convertToDTO(userRepository.flush(existing));


    }
    public AdminUserDTO convertToDTO(User user) {
         return modelMapper.map(user, AdminUserDTO.class);
    }
    public <T> T convertToEntity(Object dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

}
