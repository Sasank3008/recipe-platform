package com.user.UserService.service;

import com.user.UserService.handler.UserIdNotFoundException;
import com.user.UserService.dao.CountryRepository;
import com.user.UserService.dao.RegionRepository;
import com.user.UserService.dao.UserRepository;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.Country;
import com.user.UserService.entity.Region;
import com.user.UserService.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserProfileImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Optional<UserDTO> getUserById(Long id) throws UserIdNotFoundException {

        return Optional.ofNullable(userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new UserIdNotFoundException("user Not found with id" + id)));
    }
//
//    public UserDTO createUser(UserDTO userDTO) {
//        User user = convertToEntity(userDTO,User.class);
//      //  user.setTimeOfRegistration(LocalTime.now());
//        user = userRepository.save(user);
//        return convertToDTO(user);
//    }

    public UserDTO updateUser(Long id, UserDTO userDTO) throws UserIdNotFoundException{

        return userRepository.findById(id).map(existingUser -> {
            modelMapper.map(userDTO, existingUser);
         ///   existingUser.setCountry(countryRepository.findById(userDTO.getCountry().getId()).orElse(null));
         //   existingUser.setRegion(regionRepository.findById(userDTO.getRegion().getId()).orElse(null));
          existingUser.setRegion(convertToEntity(userDTO.getRegion(), Region.class));
          existingUser.setCountry(convertToEntity(userDTO.getCountry(), Country.class));
//           existingUser.setTimeOfRegistration(LocalTime.now());
          //  existingUser.setCountry(userDTO.getCountry());
            return convertToDTO(userRepository.save(existingUser));
        }).orElseThrow(()->new UserIdNotFoundException("no user Found with id "+id));
    }


     public UserDTO convertToDTO(User user) {
         return modelMapper.map(user, UserDTO.class);
    }

    public <T> T convertToEntity(Object dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

}
