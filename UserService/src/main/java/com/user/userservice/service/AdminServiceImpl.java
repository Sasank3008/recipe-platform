package com.user.userservice.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.userservice.dto.AdminDTO;
import com.user.userservice.dto.AdminUserDTO;
import com.user.userservice.entity.User;
import com.user.userservice.exception.UserIdNotFoundException;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.user.userservice.entity.Country;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    @Transactional
    public AdminUserDTO updateUser(Long id, AdminUserDTO userDTO) throws UserIdNotFoundException{
        return userRepository.findById(id).map(existingUser -> {
           existingUser= modelMapper.map(userDTO, User.class);
           existingUser.setId(id);
           Country country = countryRepository.findById(userDTO.getCountry().getId()).get();
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
    @Override
    public List<AdminDTO> fetchAllUsers() {
        List<User> userEntities = userRepository.findByRole("USER");
        return userEntities.stream()
                .map(user -> new AdminDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getDate(),
                        user.getEnabled(),
                        user.getCountry().getName() // Get only country name
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean toggleUserStatus(Long userId) throws UserIdNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserIdNotFoundException("User not found with ID: " + userId));
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
        return user.getEnabled();
    }
}
