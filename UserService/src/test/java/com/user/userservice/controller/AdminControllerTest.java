package com.user.userservice.controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;
import java.util.List;

import com.user.userservice.dto.CuisineDTO;
import com.user.userservice.cuisineserviceclient.RecipeServiceClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AdminController.class)
@ExtendWith(MockitoExtension.class)
class AdminControllerTest{

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RecipeServiceClient recipeServiceClient;

    @InjectMocks
    private AdminController adminController;

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
    void testDeleteCuisineSuccess() throws Exception {
        Long id = 1L;
        doNothing().when(recipeServiceClient).deleteCuisine(id);
        when(recipeServiceClient.doesCuisineExistById(id)).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(delete("/admins/cuisines/{id}", id))
                .andExpect(status().isOk());
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
    void testToggleCuisineEnabledFromEnabledToDisabled() throws Exception {
        Long id = 1L;
        when(recipeServiceClient.isCuisineEnabled(id)).thenReturn(true);
        doNothing().when(recipeServiceClient).disableCuisine(id);
        when(recipeServiceClient.doesCuisineExistById(id)).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(put("/admins/toggle-cuisine/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Cuisine disabled successfully"));
    }

    @Test
    void testToggleCuisineEnabledFromDisabledToEnabled() throws Exception {
        Long id = 1L;
        when(recipeServiceClient.isCuisineEnabled(id)).thenReturn(false);
        doNothing().when(recipeServiceClient).enableCuisine(id);
        when(recipeServiceClient.doesCuisineExistById(id)).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(put("/admins/toggle-cuisine/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Cuisine enabled successfully"));
    }
    @Test
    void testSaveCuisineDuplicateNameException() throws Exception {
        CuisineDTO newCuisine = new CuisineDTO(null, "Italian", true);
        when(recipeServiceClient.doesCuisineExistByName("Italian")).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(post("/admins/cuisines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCuisine)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.response").value("A cuisine with the name 'Italian' already exists."));
    }




}