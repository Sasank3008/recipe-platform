package com.recipe.recipeservice.service;

import com.recipe.recipeservice.dto.ApiResponse;
import com.recipe.recipeservice.dto.FavouritesRecipeResponse;
import com.recipe.recipeservice.dto.RecipeResponseDTO;
import com.recipe.recipeservice.entity.Favourites;
import com.recipe.recipeservice.entity.Recipe;
import com.recipe.recipeservice.entity.Tag;
import com.recipe.recipeservice.exception.DuplicateResourceException;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
import com.recipe.recipeservice.repository.FavoritesRepository;
import com.recipe.recipeservice.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {
    private final FavoritesRepository favoritesRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public ApiResponse addFavoriteRecipe(String userId, Long recipeId) throws ResourceNotFoundException, DuplicateResourceException {
        Favourites favourites = getOrCreateFavourites(userId);
        Recipe recipe = getRecipeById(recipeId);
        if (favourites.getFavoriteRecipes().contains(recipe)) {
            throw new DuplicateResourceException("Recipe is already in favorites");
        }
        favourites.getFavoriteRecipes().add(recipe);
        favoritesRepository.save(favourites);
        return buildApiResponse("Recipe added to favorites successfully.");
    }

    @Override
    public ApiResponse deleteFavoriteRecipe(String userId, Long recipeId) throws ResourceNotFoundException {
        Favourites favourites = getFavoritesByUserId(userId);
        Recipe recipe = getRecipeById(recipeId);
        if (!favourites.getFavoriteRecipes().remove(recipe)) {
            throw new ResourceNotFoundException("Recipe not found in user's favorites");
        }
        favoritesRepository.save(favourites);
        return buildApiResponse("Recipe removed from favorites successfully.");
    }

    @Override
    public FavouritesRecipeResponse getFavoriteRecipes(String userId) throws ResourceNotFoundException {
        Favourites favourites = getFavoritesByUserId(userId);
        List<RecipeResponseDTO> recipeResponsDTOS = favourites.getFavoriteRecipes().stream()
                .map(this::mapToRecipeResponse)
                .collect(Collectors.toList());
        return FavouritesRecipeResponse.builder()
                .recipes(recipeResponsDTOS)
                .build();
    }

    public Favourites getOrCreateFavourites(String userId) {
        return favoritesRepository.findByUserId(userId)
                .orElse(new Favourites(null, userId, new ArrayList<>()));
    }

    public Favourites getFavoritesByUserId(String userId) throws ResourceNotFoundException {
        return favoritesRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorites not found for user"));
    }

    public Recipe getRecipeById(Long recipeId) throws ResourceNotFoundException {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    }

    private ApiResponse buildApiResponse(String message) {
        return ApiResponse.builder()
                .response(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public  RecipeResponseDTO mapToRecipeResponse(Recipe recipe) {
        return RecipeResponseDTO.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .ingredients(recipe.getIngredients())
                .description(recipe.getDescription())
                .category(recipe.getCategory().getName())
                .cuisine(recipe.getCuisine().getName())
                .cookingTime(recipe.getCookingTime())
                .imageUrl(recipe.getImageUrl())
                .tags(recipe.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                .difficultyLevel(recipe.getDifficultyLevel().name())
                .status(recipe.getStatus().name())
                .dietaryRestrictions(recipe.getDietaryRestrictions())
                .build();
    }
}
