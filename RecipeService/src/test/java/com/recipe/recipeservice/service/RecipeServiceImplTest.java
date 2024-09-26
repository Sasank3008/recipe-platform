package com.recipe.recipeservice.service;

import com.recipe.recipeservice.constants.ErrorConstants;
import com.recipe.recipeservice.dto.RecipeDTO;
import com.recipe.recipeservice.dto.ViewRecipeDTO;
import com.recipe.recipeservice.entity.*;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
import com.recipe.recipeservice.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRecipe_Success() throws ResourceNotFoundException {
        Long recipeId = 1L;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setName("Spaghetti Carbonara");
        mockRecipe.setIngredients("Spaghetti, Eggs, Pancetta");
        mockRecipe.setDescription("Classic Italian pasta dish");
        mockRecipe.setCookingTime(30);
        mockRecipe.setImageUrl("http://example.com/carbonara.jpg");
        Category mockCategory = new Category();
        mockCategory.setName("Non-Veg");
        mockRecipe.setCategory(mockCategory);
        Cuisine mockCuisine = new Cuisine();
        mockCuisine.setName("Italian");
        mockRecipe.setCuisine(mockCuisine);
        Tag tag1 = new Tag();
        tag1.setName("Dinner");
        Tag tag2 = new Tag();
        tag2.setName("Easy");
        mockRecipe.setTags(List.of(tag1, tag2));
        mockRecipe.setDifficultyLevel(DifficultyLevel.EASY);
        mockRecipe.setDietaryRestrictions("None");
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));
        ViewRecipeDTO result = recipeService.getRecipe(recipeId);
        assertNotNull(result);
        assertEquals("Spaghetti Carbonara", result.getName());
        assertEquals("Spaghetti, Eggs, Pancetta", result.getIngredients());
        assertEquals("Classic Italian pasta dish", result.getDescription());
        assertEquals(30, result.getCookingTime());
        assertEquals("Italian", result.getCuisine());
        assertEquals("Non-Veg", result.getCategory());
        assertEquals(List.of("Dinner", "Easy"), result.getTags());
        assertEquals("EASY", result.getDifficultyLevel());
        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void getRecipe_ThrowsResourceNotFoundException() {
        Long invalidRecipeId = 999L;
        when(recipeRepository.findById(invalidRecipeId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> recipeService.getRecipe(invalidRecipeId));
        assertEquals(ErrorConstants.RECIPE_ID_NOT_FOUND + " " + invalidRecipeId, exception.getMessage());
        verify(recipeRepository, times(1)).findById(invalidRecipeId);
    }

    @Test
    void testSearchRecipes_ReturnsRecipeDTOs() {
        String keyword = "chicken";
        List<Recipe> recipes = List.of(new Recipe());
        when(recipeRepository.findByKeyword(keyword)).thenReturn(recipes);
        when(modelMapper.map(any(Recipe.class), eq(RecipeDTO.class))).thenReturn(new RecipeDTO());
        List<RecipeDTO> result = recipeService.searchRecipes(keyword);
        assertFalse(result.isEmpty());
        verify(recipeRepository).findByKeyword(keyword);
        verify(modelMapper, times(recipes.size())).map(any(Recipe.class), eq(RecipeDTO.class));
    }
}
