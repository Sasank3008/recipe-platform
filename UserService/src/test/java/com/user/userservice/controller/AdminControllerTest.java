package com.user.userservice.controller;
import com.user.userservice.cuisineserviceclient.RecipeServiceClient;
import com.user.userservice.dto.CountryDTO;
import com.user.userservice.dto.CuisineDTO;
import com.user.userservice.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import com.user.userservice.dto.AdminUserDTO;
import com.user.userservice.exception.UserIdNotFoundException;
import com.user.userservice.service.AdminService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AdminService adminService;
    @Mock
    private RecipeServiceClient recipeServiceClient;
    @InjectMocks
    private AdminController adminController;
    @Mock
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(adminController).build();
    }

    @Test
    void testGetAllCuisines() throws Exception {
        List<CuisineDTO> cuisines = Arrays.asList(new CuisineDTO(1L, "Italian", true));
        when(recipeServiceClient.getAllCuisines()).thenReturn(cuisines);

        mockMvc.perform(get("/admins/cuisines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Italian"));
    }
    @Test
    void testEditUser_Success() throws UserIdNotFoundException {
        Long userId = 1L;
        AdminUserDTO userDTO = new AdminUserDTO();
        AdminUserDTO updatedUserDTO = new AdminUserDTO();
        when(adminService.updateUser(userId, userDTO)).thenReturn(updatedUserDTO);
        ResponseEntity<AdminUserDTO> response = adminController.editUser(userId, userDTO);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUserDTO, response.getBody());
        verify(adminService).updateUser(userId, userDTO);
    }
    @Test
    void testEditUser_NotFound() throws UserIdNotFoundException {
        Long userId = 1L;
        AdminUserDTO userDTO = new AdminUserDTO();
        when(adminService.updateUser(userId, userDTO)).thenReturn(null);
        ResponseEntity<AdminUserDTO> response = adminController.editUser(userId, userDTO);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(adminService).updateUser(userId, userDTO);
    }
    @Test
    void testEditUser_ThrowsException() throws UserIdNotFoundException {
        Long userId = 1L;
        AdminUserDTO userDTO = new AdminUserDTO();
        when(adminService.updateUser(userId, userDTO)).thenThrow(new UserIdNotFoundException("User not found"));
        assertThrows(UserIdNotFoundException.class, () -> {
            adminController.editUser(userId, userDTO);
        });
        verify(adminService).updateUser(userId, userDTO);
    }
    @Test
    void testDisableCuisineSuccess() throws Exception {
        Long id = 1L;
        doNothing().when(recipeServiceClient).disableCuisine(id);
        when(recipeServiceClient.doesCuisineExistById(id)).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(put("/admins/cuisines/{id}/disable", id))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCuisineSuccess() throws Exception {
        Long id = 1L;
        doNothing().when(recipeServiceClient).deleteCuisine(id);
        when(recipeServiceClient.doesCuisineExistById(id)).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(delete("/admins/cuisines/{id}", id))
                .andExpect(status().isOk());
    }
    @Test
    void testEnableCuisineSuccess() throws Exception {
        Long id = 1L;
        doNothing().when(recipeServiceClient).enableCuisine(id);
        when(recipeServiceClient.doesCuisineExistById(id)).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(put("/admins/cuisines/{id}/enable", id))
                .andExpect(status().isOk());
    }
    @Test
    void testSaveCuisineSuccess() throws Exception {
        CuisineDTO newCuisine = new CuisineDTO(null, "Mexican", true);
        CuisineDTO addedCuisine = new CuisineDTO(1L, "Mexican", true);

        when(recipeServiceClient.doesCuisineExistByName("Mexican")).thenReturn(ResponseEntity.ok(false));
        when(recipeServiceClient.addCuisine(any(CuisineDTO.class))).thenReturn(addedCuisine);

        mockMvc.perform(post("/admins/cuisines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCuisine)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mexican"));
    }
    @Test
    void testUpdateCuisineSuccess() throws Exception {
        Long id = 1L;
        CuisineDTO originalCuisine = new CuisineDTO(id, "Italian", true);
        CuisineDTO updatedCuisine = new CuisineDTO(id, "Italian Updated", true);

        when(recipeServiceClient.doesCuisineExistById(id)).thenReturn(ResponseEntity.ok(true));
        when(recipeServiceClient.updateCuisine(eq(id), any(CuisineDTO.class))).thenReturn(updatedCuisine);

        mockMvc.perform(put("/admins/cuisines/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalCuisine)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Italian Updated"));
    }
    @Test
    void testGetCountries() throws Exception {
        CountryDTO country1 = new CountryDTO(1L, "Usa");
        CountryDTO country2 = new CountryDTO(2L, "Canada");
        List<CountryDTO> countries = Arrays.asList(country1, country2);
        when(countryService.fetchCountries()).thenReturn(countries);
        mockMvc.perform(get("/admins/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Usa"))
                .andExpect(jsonPath("$[1].name").value("Canada"));
        verify(countryService).fetchCountries();
    }
    @Test
    void testAddCountry() throws Exception {
        // Arrange
        CountryDTO newCountry = new CountryDTO(null, "Mexico");
        CountryDTO savedCountry = new CountryDTO(3L, "Mexico");
        when(countryService.saveCountry(any(CountryDTO.class))).thenReturn(savedCountry);
        mockMvc.perform(post("/admins/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mexico\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mexico"))
                .andExpect(jsonPath("$.id").value(3));
        verify(countryService).saveCountry(any(CountryDTO.class));
    }
    }