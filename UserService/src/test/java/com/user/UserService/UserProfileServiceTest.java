package com.user.UserService;
import com.user.UserService.handler.UserIdNotFoundException;
import com.user.UserService.dto.CountryDTO;
import com.user.UserService.dto.RegionDTO;
import com.user.UserService.dto.UserDTO;
import com.user.UserService.entity.User;
import com.user.UserService.entity.Country;
import com.user.UserService.entity.Region;
import com.user.UserService.dao.UserRepository;
import com.user.UserService.service.UserProfileImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserProfileImpl userProfileService;

    // Helper methods to create test objects
    private UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setEnabled(true);
        userDTO.setCountry(new CountryDTO(1L, "USA"));
        userDTO.setRegion(new RegionDTO(1L, "California"));
        userDTO.setPassword("encryptedPassword");
        return userDTO;
    }
    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setEnabled(true);
        user.setCountry(new Country(1L, "USA"));
        user.setRegion(new Region(1L, "California"));
        user.setPassword("encryptedPassword");
        return user;
    }
//    @Test
//    void testUpdateUser_Success() {
//        // Arrange
//        Long userId = 1L;
//        UserDTO userDTO = createUserDTO();
//        User existingUser = createUser();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        doAnswer(invocation -> {
//            User target = invocation.getArgument(1);
//            UserDTO source = invocation.getArgument(0);
//            target.setFirstName(source.getFirstName());
//            target.setLastName(source.getLastName());
//            target.setEmail(source.getEmail());
//            target.setEnabled(source.getEnabled());
//            target.setPassword(source.getPassword());
//            // Assume these methods handle conversion from DTO to entity
//            target.setCountry(new Country(source.getCountry().getId(), source.getCountry().getName()));
//            target.setRegion(new Region(source.getRegion().getId(), source.getRegion().getName()));
//            return null;
//        }).when(modelMapper).map(any(UserDTO.class), any(User.class));
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//        when(userProfileService.convertToDTO(any(User.class))).thenReturn(userDTO);
//
//        // Act
//        UserDTO result = userProfileService.updateUser(userId, userDTO);
//
//        // Assert
//        assertNotNull(result);
//       // assertEquals(userId, result.getId());
//        assertEquals("John", result.getFirstName());
//        assertEquals("Doe", result.getLastName());
//        assertEquals("john.doe@example.com", result.getEmail());
//        assertTrue(result.getEnabled());
//        assertEquals("USA", result.getCountry().getName());
//        assertEquals("California", result.getRegion().getName());
//        verify(userRepository).save(any(User.class));
//    }
    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = createUserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UserIdNotFoundException.class, () -> {
            userProfileService.updateUser(userId, userDTO);
        });

        // Additional assertion to check the message of the exception
        assertEquals("no user Found with id " + userId, exception.getMessage());

        // Verify that save method is never called since user is not found
        verify(userRepository, never()).save(any());
    }





}

