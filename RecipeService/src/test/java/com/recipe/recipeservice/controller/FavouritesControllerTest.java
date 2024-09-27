package com.recipe.recipeservice.controller;
import com.recipe.recipeservice.dto.ApiResponse;
import com.recipe.recipeservice.dto.FavouritesRecipeResponse;
import com.recipe.recipeservice.exception.DuplicateResourceException;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
import com.recipe.recipeservice.service.FavouritesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FavouritesControllerTest {

    @Mock
    private FavouritesService favouritesService;

    @InjectMocks
    private FavouritesController favouritesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFavoriteRecipe_Success() throws ResourceNotFoundException, DuplicateResourceException {
        String userId = "1";
        Long recipeId = 123L;
        ApiResponse apiResponse = ApiResponse.builder()
                .response("Recipe added to favorites successfully.")
                .build();
        when(favouritesService.addFavoriteRecipe(anyString(), anyLong())).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> response = favouritesController.addFavoriteRecipe(userId, recipeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Recipe added to favorites successfully.", response.getBody().getResponse());
        verify(favouritesService, times(1)).addFavoriteRecipe(userId, recipeId);
    }

    @Test
    void addFavoriteRecipe_DuplicateResourceException() throws ResourceNotFoundException, DuplicateResourceException {
        String userId = "1";
        Long recipeId = 123L;
        when(favouritesService.addFavoriteRecipe(anyString(), anyLong()))
                .thenThrow(new DuplicateResourceException("Recipe is already in favorites"));
        DuplicateResourceException exception = null;
        try {
            favouritesController.addFavoriteRecipe(userId, recipeId);
        } catch (DuplicateResourceException ex) {
            exception = ex;
        }
        assertEquals("Recipe is already in favorites", exception.getMessage());
        verify(favouritesService, times(1)).addFavoriteRecipe(userId, recipeId);
    }

    @Test
    void deleteFavoriteRecipe_Success() throws ResourceNotFoundException {
        String userId = "1";
        Long recipeId = 123L;
        ApiResponse apiResponse = ApiResponse.builder()
                .response("Recipe removed from favorites successfully.")
                .build();
        when(favouritesService.deleteFavoriteRecipe(anyString(), anyLong())).thenReturn(apiResponse);
        ResponseEntity<ApiResponse> response = favouritesController.deleteFavoriteRecipe(userId, recipeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Recipe removed from favorites successfully.", response.getBody().getResponse());
        verify(favouritesService, times(1)).deleteFavoriteRecipe(userId, recipeId);
    }

    @Test
    void deleteFavoriteRecipe_ResourceNotFoundException() throws ResourceNotFoundException {
        String userId = "1";
        Long recipeId = 123L;
        when(favouritesService.deleteFavoriteRecipe(anyString(), anyLong()))
                .thenThrow(new ResourceNotFoundException("Recipe not found in user's favorites"));
        ResourceNotFoundException exception = null;
        try {
            favouritesController.deleteFavoriteRecipe(userId, recipeId);
        } catch (ResourceNotFoundException ex) {
            exception = ex;
        }
        assertEquals("Recipe not found in user's favorites", exception.getMessage());
        verify(favouritesService, times(1)).deleteFavoriteRecipe(userId, recipeId);
    }

    @Test
    void getFavoriteRecipes_Success() throws ResourceNotFoundException {
        String userId = "1";
        FavouritesRecipeResponse responseDTO = FavouritesRecipeResponse.builder()
                .recipes(Collections.emptyList())
                .build();
        when(favouritesService.getFavoriteRecipes(anyString())).thenReturn(responseDTO);
        ResponseEntity<FavouritesRecipeResponse> response = favouritesController.getFavoriteRecipes(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getRecipes().size());
        verify(favouritesService, times(1)).getFavoriteRecipes(userId);
    }

    @Test
    void getFavoriteRecipes_ResourceNotFoundException() throws ResourceNotFoundException {
        String userId = "1";
        when(favouritesService.getFavoriteRecipes(anyString()))
                .thenThrow(new ResourceNotFoundException("Favorites not found for user"));
        ResourceNotFoundException exception = null;
        try {
            favouritesController.getFavoriteRecipes(userId);
        } catch (ResourceNotFoundException ex) {
            exception = ex;
        }
        assertEquals("Favorites not found for user", exception.getMessage());
        verify(favouritesService, times(1)).getFavoriteRecipes(userId);
    }
}
