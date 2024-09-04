package com.user.UserService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.user.UserService.Entity.CuisineDTO;
import com.user.UserService.RecipeSeviceClient.RecipeServiceClient;
import com.user.UserService.controller.AdminCuisineController;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
@ExtendWith(MockitoExtension.class)
class UserServiceApplicationTest{

	private MockMvc mockMvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private RecipeServiceClient recipeServiceClient;

	@InjectMocks
	private AdminCuisineController adminCuisineController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(adminCuisineController).build();
	}



	@Test
	void testGetEnabledCuisines() throws Exception {
		List<CuisineDTO> cuisines = Arrays.asList(new CuisineDTO(1L, "Italian", true));
		when(recipeServiceClient.getEnabledCuisines()).thenReturn(cuisines);

		mockMvc.perform(get("/admin/cuisines/enabled"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Italian"));
	}

	@Test
	void testDisableCuisine() throws Exception {
		doNothing().when(recipeServiceClient).disableCuisine(1L);

		mockMvc.perform(put("/admin/cuisines/disable/1"))
				.andExpect(status().isOk());
	}

	@Test
	void testDeleteCuisine() throws Exception {
		doNothing().when(recipeServiceClient).deleteCuisine(1L);

		mockMvc.perform(delete("/admin/cuisines/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void testEnableCuisine() throws Exception {
		doNothing().when(recipeServiceClient).enableCuisine(1L);

		mockMvc.perform(put("/admin/cuisines/enable/1"))
				.andExpect(status().isOk());
	}


	@Test
	void testViewAllCuisines() throws Exception {
		List<CuisineDTO> cuisines = Arrays.asList(new CuisineDTO(1L, "Italian", true));
		when(recipeServiceClient.getAllCuisines()).thenReturn(cuisines);

		mockMvc.perform(get("/admin/cuisines/view"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Italian"));
	}

	// Example of exception handling test
	@Test
	void testDisableCuisineNotFound() throws Exception {
		doThrow(FeignException.NotFound.class).when(recipeServiceClient).disableCuisine(1L);

		mockMvc.perform(put("/admin/cuisines/disable/1"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testAddCuisineSuccess() throws Exception {
		CuisineDTO newCuisine = new CuisineDTO(null, "Mexican", true);
		CuisineDTO addedCuisine = new CuisineDTO(1L, "Mexican", true);

		when(recipeServiceClient.addCuisine(any(CuisineDTO.class))).thenReturn(addedCuisine);

		mockMvc.perform(post("/admin/cuisines/add")
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

		when(recipeServiceClient.updateCuisine(eq(id), any(CuisineDTO.class))).thenReturn(updatedCuisine);

		mockMvc.perform(put("/admin/cuisines/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(originalCuisine)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Italian Updated"));
	}



}