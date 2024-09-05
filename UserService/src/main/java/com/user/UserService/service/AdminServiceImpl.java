package com.user.UserService.service;
import com.user.UserService.handler.UserIdNotFoundException;
import com.user.UserService.repository.CountryRepository;
import com.user.UserService.repository.UserRepository;
import com.user.UserService.dto.AdminUserDTO;
import com.user.UserService.entity.Country;
import com.user.UserService.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    public AdminUserDTO updateUser(Long id, AdminUserDTO userDTO) throws UserIdNotFoundException{
        return userRepository.findById(id).map(existingUser -> {
       /// modelMapper.map(existingUser, User.class);
            Country country = countryRepository.findByName(userDTO.getCountry().getName()).get();
            existingUser.setCountry(country);
        return convertToDTO(userRepository.save(existingUser));
        }).orElseThrow(()->new UserIdNotFoundException("User ID not found with id "+id));
    }
    public AdminUserDTO convertToDTO(User user) {
         return modelMapper.map(user, AdminUserDTO.class);
    }
    public <T> T convertToEntity(Object dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

}
