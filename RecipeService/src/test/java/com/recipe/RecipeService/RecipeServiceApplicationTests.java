package com.recipe.RecipeService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.recipe.RecipeService.Entity.Cuisine;
import com.recipe.RecipeService.Entity.CuisineDTO;
import com.recipe.RecipeService.Model.CuisineRepository;
import com.recipe.RecipeService.controller.RecipeController;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class RecipeServiceApplicationTests {

	private MockMvc mockMvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private CuisineRepository cuisineRepository;

	@InjectMocks
	private RecipeController recipeController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
	}

	@Test
	void testAddCuisine_Success() throws Exception {
		CuisineDTO cuisineDTO = new CuisineDTO(null, "Italian", true);
		Cuisine savedCuisine = new Cuisine(1L, "Italian", true);

		when(cuisineRepository.findByName("Italian")).thenReturn(Optional.empty());
		when(cuisineRepository.save(any(Cuisine.class))).thenReturn(savedCuisine);

		mockMvc.perform(post("/api/recipes/cuisines")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cuisineDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Italian"));
	}

	@Test
	void testAddCuisine_Conflict() throws Exception {
		CuisineDTO cuisineDTO = new CuisineDTO(null, "Italian", true);

		when(cuisineRepository.findByName("Italian")).thenReturn(Optional.of(new Cuisine()));

		mockMvc.perform(post("/api/recipes/cuisines")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cuisineDTO)))
				.andExpect(status().isConflict());
	}

	@Test
	void testGetEnabledCuisines() throws Exception {
		List<Cuisine> cuisines = Arrays.asList(new Cuisine(1L, "Italian", true));
		when(cuisineRepository.findByIsEnabled(true)).thenReturn(cuisines);

		mockMvc.perform(get("/api/recipes/cuisines/enabled"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Italian"));
	}

	@Test
	void testDisableCuisine_Success() throws Exception {
		Cuisine cuisine = new Cuisine(1L, "Italian", true);
		when(cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));
		when(cuisineRepository.save(cuisine)).thenReturn(cuisine);

		mockMvc.perform(put("/api/recipes/cuisines/disable/1"))
				.andExpect(status().isOk());
	}

	@Test
	void testDisableCuisine_NotFound() throws Exception {
		when(cuisineRepository.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(put("/api/recipes/cuisines/disable/1"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteCuisine_Success() throws Exception {
		Cuisine cuisine = new Cuisine(1L, "Italian", true);
		when(cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));
		doNothing().when(cuisineRepository).deleteById(1L);

		mockMvc.perform(delete("/api/recipes/cuisines/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void testDeleteCuisine_NotFound() throws Exception {
		when(cuisineRepository.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(delete("/api/recipes/cuisines/1"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdateCuisine_Success() throws Exception {
		CuisineDTO cuisineDTO = new CuisineDTO(1L, "Updated Italian", true);
		Cuisine existingCuisine = new Cuisine(1L, "Italian", true);

		when(cuisineRepository.findById(1L)).thenReturn(Optional.of(existingCuisine));
		when(cuisineRepository.save(any(Cuisine.class))).thenReturn(existingCuisine);

		mockMvc.perform(put("/api/recipes/cuisines/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cuisineDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Italian"));
	}

	@Test
	void testUpdateCuisine_NotFound() throws Exception {
		CuisineDTO cuisineDTO = new CuisineDTO(1L, "Updated Italian", true);
		when(cuisineRepository.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(put("/api/recipes/cuisines/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cuisineDTO)))
				.andExpect(status().isNotFound());
	}
	@Test
	void testGetAllCuisinesSuccess() throws Exception {
		// Setup mock data
		List<Cuisine> mockCuisines = Arrays.asList(
				new Cuisine(1L, "Italian", true),
				new Cuisine(2L, "Mexican", true)
		);
		List<CuisineDTO> expectedDTOs = mockCuisines.stream()
				.map(cuisine -> new CuisineDTO(cuisine.getId(), cuisine.getName(), cuisine.isEnabled()))
				.collect(Collectors.toList());

		// Mocking the repository or service call
		when(cuisineRepository.getAllCuisines()).thenReturn(mockCuisines);

		// Perform the request and assert results
		mockMvc.perform(get("/api/recipes/cuisines")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(expectedDTOs)));
	}
	@Test
	void testEnableCuisineSuccess() throws Exception {
		Long id = 1L;
		Cuisine existingCuisine = new Cuisine(id, "Italian", false);

		when(cuisineRepository.findById(id)).thenReturn(Optional.of(existingCuisine));
		when(cuisineRepository.save(existingCuisine)).thenReturn(existingCuisine);

		mockMvc.perform(put("/api/recipes/cuisines/enable/{id}", id)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
