package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.dto.ApiResponse;
import com.recipe.recipeservice.dto.FavouritesRecipeResponse;
import com.recipe.recipeservice.exception.DuplicateResourceException;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
import com.recipe.recipeservice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class FavouritesController {

    private final RecipeService recipeService;

    @PostMapping("/favourites/{userId}/add")
    public ResponseEntity<ApiResponse> addFavoriteRecipe(@PathVariable String userId, @RequestBody Long recipeId) throws ResourceNotFoundException, DuplicateResourceException {
        return ResponseEntity.ok(recipeService.addFavoriteRecipe(userId, recipeId));
    }

    @DeleteMapping("/favourites/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteFavoriteRecipe(@PathVariable String userId, @RequestBody Long recipeId) throws ResourceNotFoundException {
        return ResponseEntity.ok(recipeService.deleteFavoriteRecipe(userId, recipeId));
    }

    @GetMapping("/favourites/{userId}")
    public ResponseEntity<FavouritesRecipeResponse> getFavoriteRecipes(@PathVariable String userId) throws ResourceNotFoundException {
        FavouritesRecipeResponse favouritesResponse = recipeService.getFavoriteRecipes(userId);
        return ResponseEntity.ok(favouritesResponse);
    }

}
