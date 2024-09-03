package com.admin.AdminService;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceApplicationTests {

	@Mock
	private RecipeClient recipeClient;

	@InjectMocks
	private AdminController adminController;

	@Test
	void addCuisine_ShouldReturnCuisineDTO() {
		CuisineDTO requestDTO = new CuisineDTO(null, "Italian", true);
		CuisineDTO responseDTO = new CuisineDTO(1L, "Italian", true);
		when(recipeClient.addCuisine(requestDTO)).thenReturn(responseDTO);

		ResponseEntity<CuisineDTO> response = adminController.addCuisine(requestDTO);

		assertEquals(200, response.getStatusCodeValue());
		assertNotNull(response.getBody());
		assertEquals("Italian", response.getBody().getName());
		assertTrue(response.getBody().isEnabled());
	}

	@Test
	void addCuisine_ShouldHandleException() {
		CuisineDTO requestDTO = new CuisineDTO(null, "Italian", true);
		when(recipeClient.addCuisine(requestDTO)).thenThrow(new RuntimeException("Service Down"));

		ResponseEntity<CuisineDTO> response = adminController.addCuisine(requestDTO);

		assertEquals(500, response.getStatusCodeValue());
	}

	@Test
	void enableCuisine_ShouldReturnOk() {
		doNothing().when(recipeClient).enableCuisine("Italian");

		ResponseEntity<Void> response = adminController.enableCuisine("Italian");

		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	void enableCuisine_ShouldHandleException() {
		doThrow(new RuntimeException("Service Down")).when(recipeClient).enableCuisine("Italian");

		ResponseEntity<Void> response = adminController.enableCuisine("Italian");

		assertEquals(500, response.getStatusCodeValue());
	}

	@Test
	void getAllCuisines_ShouldReturnListOfCuisines() {
		List<CuisineDTO> cuisines = Arrays.asList(
				new CuisineDTO(1L, "Italian", true),
				new CuisineDTO(2L, "Mexican", true)
		);
		when(recipeClient.getAllCuisines()).thenReturn(cuisines);

		ResponseEntity<List<CuisineDTO>> response = adminController.getAllCuisines();

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(2, response.getBody().size());
		assertEquals("Italian", response.getBody().get(0).getName());
	}

	@Test
	void getAllCuisines_ShouldHandleException() {
		when(recipeClient.getAllCuisines()).thenThrow(new RuntimeException("Service Down"));

		ResponseEntity<List<CuisineDTO>> response = adminController.getAllCuisines();

		assertEquals(500, response.getStatusCodeValue());
	}

	@Test
	void getEnabledCuisineNames_ShouldReturnNames() {
		List<String> names = Arrays.asList("Italian", "Mexican");
		when(recipeClient.getEnabledCuisineNames()).thenReturn(names);

		ResponseEntity<List<String>> response = adminController.getEnabledCuisineNames();

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(2, response.getBody().size());
		assertTrue(response.getBody().contains("Italian"));
	}

	@Test
	void getEnabledCuisineNames_ShouldHandleException() {
		when(recipeClient.getEnabledCuisineNames()).thenThrow(new RuntimeException("Service Down"));

		ResponseEntity<List<String>> response = adminController.getEnabledCuisineNames();

		assertEquals(500, response.getStatusCodeValue());
	}
}