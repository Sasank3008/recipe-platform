package com.user.UserService.service;
import com.user.UserService.dto.AdminUserDTO;
import com.user.UserService.dto.CountryDTO;
import com.user.UserService.entity.Country;
import com.user.UserService.entity.User;
import com.user.UserService.handler.UserIdNotFoundException;
import com.user.UserService.repository.CountryRepository;
import com.user.UserService.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void testUpdateUser() throws UserIdNotFoundException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        AdminUserDTO userDTO = new AdminUserDTO("John", "Doe", "john@example.com", true, new CountryDTO(1L, "USA"), "Region");
        Country country = new Country(1L, "USA");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(countryRepository.findByName("USA")).thenReturn(Optional.of(country));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(AdminUserDTO.class))).thenReturn(userDTO);
        AdminUserDTO result = adminService.updateUser(userId, userDTO);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepository).findById(userId);
        verify(countryRepository).findByName("USA");
        verify(userRepository).save(any(User.class));
    }
    @Test
    void testConvertToDTO() {
        User user = new User();
        AdminUserDTO userDTO = new AdminUserDTO();
        when(modelMapper.map(user, AdminUserDTO.class)).thenReturn(userDTO);
        AdminUserDTO result = adminService.convertToDTO(user);
        assertNotNull(result);
        verify(modelMapper).map(user, AdminUserDTO.class);
    }
    @Test
    void testConvertToEntity() {
        AdminUserDTO userDTO = new AdminUserDTO();
        User user = new User();
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        User result = adminService.convertToEntity(userDTO, User.class);
        assertNotNull(result);
        verify(modelMapper).map(userDTO, User.class);
    }
}
