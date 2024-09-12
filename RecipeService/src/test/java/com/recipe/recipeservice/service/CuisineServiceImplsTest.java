package com.recipe.recipeservice.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;
import com.recipe.recipeservice.dto.CuisineDTO;
import com.recipe.recipeservice.entity.Cuisine;
import com.recipe.recipeservice.repository.CuisineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class CuisineServiceImplsTest {
	@Mock
	private CuisineRepository cuisineRepository;
	@InjectMocks
	private CuisineServiceImpl cuisineService;
	private CuisineDTO cuisineDTO;
	private Cuisine cuisine;

	@BeforeEach
	public void setUp() {
		cuisineDTO = new CuisineDTO(1L, "Italian", true, "url");
		cuisine = new Cuisine();
		cuisine.setId(1L);
		cuisine.setName("Italian");
		cuisine.setEnabled(true);
		cuisine.setImageUrl("url");
	}

	@Test
	void testAddCuisine() {
		when(cuisineRepository.save(any(Cuisine.class))).thenReturn(cuisine);
		CuisineDTO result = cuisineService.addCuisine(cuisineDTO);
		assertNotNull(result);
		assertEquals(cuisineDTO.getName(), result.getName());
		verify(cuisineRepository, times(1)).save(any(Cuisine.class));
	}

	@Test
	void testDisableCuisineById() {
		when(cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));
		when(cuisineRepository.save(any(Cuisine.class))).thenReturn(cuisine);
		boolean result = cuisineService.disableCuisineById(1L);
		assertTrue(result);
		assertFalse(cuisine.isEnabled());
		verify(cuisineRepository, times(1)).findById(1L);
		verify(cuisineRepository, times(1)).save(cuisine);
	}

	@Test
	void testEnableCuisineById() {
		when(cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));
		when(cuisineRepository.save(any(Cuisine.class))).thenReturn(cuisine);
		boolean result = cuisineService.enableCuisineById(1L);
		assertTrue(result);
		assertTrue(cuisine.isEnabled());
		verify(cuisineRepository, times(1)).findById(1L);
		verify(cuisineRepository, times(1)).save(cuisine);
	}

	@Test
	void testDeleteCuisineById() {
		when(cuisineRepository.existsById(1L)).thenReturn(true);
		boolean result = cuisineService.deleteCuisineById(1L);
		assertTrue(result);
		verify(cuisineRepository, times(1)).existsById(1L);
		verify(cuisineRepository, times(1)).deleteById(1L);
	}

	@Test
	void testUpdateCuisineById() {
		when(cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));
		when(cuisineRepository.save(any(Cuisine.class))).thenReturn(cuisine);
		CuisineDTO updatedCuisineDTO = new CuisineDTO(1L, "French", false, "new-url");
		CuisineDTO result = cuisineService.updateCuisineById(1L, updatedCuisineDTO);
		assertNotNull(result);
		assertEquals(updatedCuisineDTO.getName(), result.getName());
		assertEquals(updatedCuisineDTO.isEnabled(), result.isEnabled());
		assertEquals(updatedCuisineDTO.getImageUrl(), result.getImageUrl());
		verify(cuisineRepository, times(1)).findById(1L);
		verify(cuisineRepository, times(1)).save(any(Cuisine.class));
	}

	@Test
	void testGetEnabledCuisines() {
		List<Cuisine> cuisines = Collections.singletonList(cuisine);
		when(cuisineRepository.findByIsEnabled(true)).thenReturn(cuisines);
		List<CuisineDTO> result = cuisineService.getEnabledCuisines();
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(cuisine.getName(), result.get(0).getName());
		verify(cuisineRepository, times(1)).findByIsEnabled(true);
	}

	@Test
	void testGetAllCuisines() {
		List<Cuisine> cuisines = Collections.singletonList(cuisine);
		when(cuisineRepository.getAllCuisines()).thenReturn(cuisines);
		List<CuisineDTO> result = cuisineService.getAllCuisines();
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(cuisine.getName(), result.get(0).getName());
		verify(cuisineRepository, times(1)).getAllCuisines();
	}

	@Test
	void testDoesCuisineExistByName() {
		when(cuisineRepository.existsByName("Italian")).thenReturn(true);
		boolean result = cuisineService.doesCuisineExistByName("Italian");
		assertTrue(result);
		verify(cuisineRepository, times(1)).existsByName("Italian");
	}

	@Test
	void testDoesCuisineExistById() {
		when(cuisineRepository.existsById(1L)).thenReturn(true);
		boolean result = cuisineService.doesCuisineExistById(1L);
		assertTrue(result);
		verify(cuisineRepository, times(1)).existsById(1L);
	}

	@Test
	void testIsCuisineEnabled() {
		when(cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));
		boolean result = cuisineService.isCuisineEnabled(1L);
		assertTrue(result);
		verify(cuisineRepository, times(1)).findById(1L);
	}
}
