package com.recipe.recipeservice.service;
import com.recipe.recipeservice.dto.ApiResponse;
import com.recipe.recipeservice.dto.FavouritesRecipeResponse;
import com.recipe.recipeservice.entity.Favourites;
import com.recipe.recipeservice.entity.Recipe;
import com.recipe.recipeservice.exception.DuplicateResourceException;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
import com.recipe.recipeservice.repository.FavoritesRepository;
import com.recipe.recipeservice.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class FavouritesServiceImplTest {

    @Mock
    private FavoritesRepository favoritesRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private FavouritesServiceImpl favouritesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFavoriteRecipe_Success() throws ResourceNotFoundException, DuplicateResourceException {
        String userId = "1";
        Long recipeId = 123L;
        Favourites favourites = new Favourites(null, userId, new ArrayList<>());
        Recipe recipe = new Recipe();
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.of(favourites));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        ApiResponse response = favouritesService.addFavoriteRecipe(userId, recipeId);
        assertEquals("Recipe added to favorites successfully.", response.getResponse());
        verify(favoritesRepository, times(1)).save(favourites);
    }

    @Test
    void addFavoriteRecipe_RecipeAlreadyInFavorites() throws ResourceNotFoundException, DuplicateResourceException {
        String userId = "1";
        Long recipeId = 123L;
        Recipe recipe = new Recipe();
        Favourites favourites = new Favourites(null, userId, new ArrayList<>(Collections.singletonList(recipe)));
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.of(favourites));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            favouritesService.addFavoriteRecipe(userId, recipeId);
        });
        assertEquals("Recipe is already in favorites", exception.getMessage());
        verify(favoritesRepository, never()).save(favourites);
    }

    @Test
    void addFavoriteRecipe_RecipeNotFound() {
        String userId = "1";
        Long recipeId = 123L;
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.of(new Favourites()));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            favouritesService.addFavoriteRecipe(userId, recipeId);
        });
        assertEquals("Recipe not found", exception.getMessage());
    }

    @Test
    void deleteFavoriteRecipe_Success() throws ResourceNotFoundException {
        String userId = "1";
        Long recipeId = 123L;
        Recipe recipe = new Recipe();
        Favourites favourites = new Favourites(null, userId, new ArrayList<>(Collections.singletonList(recipe)));
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.of(favourites));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        ApiResponse response = favouritesService.deleteFavoriteRecipe(userId, recipeId);
        assertEquals("Recipe removed from favorites successfully.", response.getResponse());
        verify(favoritesRepository, times(1)).save(favourites);
    }

    @Test
    void deleteFavoriteRecipe_RecipeNotInFavorites() throws ResourceNotFoundException {
        String userId = "1";
        Long recipeId = 123L;
        Favourites favourites = new Favourites(null, userId, new ArrayList<>());
        Recipe recipe = new Recipe();
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.of(favourites));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            favouritesService.deleteFavoriteRecipe(userId, recipeId);
        });
        assertEquals("Recipe not found in user's favorites", exception.getMessage());
        verify(favoritesRepository, never()).save(favourites);
    }

    @Test
    void deleteFavoriteRecipe_RecipeNotFound() {
        String userId = "1";
        Long recipeId = 123L;
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.of(new Favourites()));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            favouritesService.deleteFavoriteRecipe(userId, recipeId);
        });
        assertEquals("Recipe not found", exception.getMessage());
    }

    @Test
    void getFavoriteRecipes_Success() throws ResourceNotFoundException {
        String userId = "1";
        Favourites favourites = new Favourites(null, userId, new ArrayList<>());
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.of(favourites));
        FavouritesRecipeResponse response = favouritesService.getFavoriteRecipes(userId);
        assertEquals(0, response.getRecipes().size());
        verify(favoritesRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getFavoriteRecipes_FavoritesNotFound() {
        String userId = "1";
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            favouritesService.getFavoriteRecipes(userId);
        });
        assertEquals("Favorites not found for user", exception.getMessage());
    }

    @Test
    void getOrCreateFavourites_NewFavourites() {
        String userId = "1";
        when(favoritesRepository.findByUserId(userId)).thenReturn(Optional.empty());
        Favourites favourites = favouritesService.getOrCreateFavourites(userId);
        assertNotNull(favourites);
        assertEquals(userId, favourites.getUserId());
        assertTrue(favourites.getFavoriteRecipes().isEmpty());
    }
}
