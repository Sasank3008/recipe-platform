package com.user.userservice.service;
import com.user.userservice.dto.AdminUserDTO;
import com.user.userservice.dto.CountryDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.entity.User;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
 class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AdminServiceImpl adminService;
    private AdminUserDTO userDTO;
    private User user;
    private User newuser;
    private Country country;
    //
    @BeforeEach
    void setUp() {
        user  = new User();
        user.setId(3L);
        country = new Country(2L,"India");
        newuser  = new User();
        newuser.setId(3L);
        newuser.setCountry(new Country(1L,"USA"));



        user.setCountry(country);
        userDTO = new AdminUserDTO();
        userDTO.setCountry(new CountryDTO(1L, "USA"));
    }

//    @Test
//    void testUpdateUser() throws UserIdNotFoundException {
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//        AdminUserDTO userDTO = new AdminUserDTO("John", "Doe", "john@example.com", true, new CountryDTO(1L, "USA"), "Region");
//        Country country = new Country(1L, "USA");
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(countryRepository.findByName("USA")).thenReturn(Optional.of(country));
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(modelMapper.map(any(User.class), eq(AdminUserDTO.class))).thenReturn(userDTO);
//        AdminUserDTO result = adminService.updateUser(userId, userDTO);
//        assertNotNull(result);
//        assertEquals("John", result.getFirstName());
//        verify(userRepository).findById(userId);
//        verify(countryRepository).findByName("USA");
//        verify(userRepository).save(any(User.class));
//    }
//    @Test
//    void whenUpdateUser_thenReturnUpdatedUser() throws UserIdNotFoundException {
//        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
//        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
//        when(modelMapper.map(userDTO, User.class)).thenReturn(newuser);
//
//        when(userRepository.save(any(User.class))).thenReturn(newuser);
//
//        AdminUserDTO result = adminService.updateUser(3L, userDTO);
//
//        assertNotNull(result);
//        verify(userRepository).save(newuser);
//        assertEquals(userDTO.getCountry().getId(), result.getCountry().getId());
//    }

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
