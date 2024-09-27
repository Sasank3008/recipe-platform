package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.dto.ApiResponse;
import com.recipe.recipeservice.dto.FavouritesRecipeResponse;
import com.recipe.recipeservice.exception.DuplicateResourceException;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
import com.recipe.recipeservice.service.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FavouritesController {

    private final FavouritesService favouritesService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<ApiResponse> addFavoriteRecipe(@PathVariable String userId, @RequestBody Long recipeId) throws ResourceNotFoundException, DuplicateResourceException {
        return ResponseEntity.ok(favouritesService.addFavoriteRecipe(userId, recipeId));
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteFavoriteRecipe(@PathVariable String userId, @RequestBody Long recipeId) throws ResourceNotFoundException {
        return ResponseEntity.ok(favouritesService.deleteFavoriteRecipe(userId, recipeId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FavouritesRecipeResponse> getFavoriteRecipes(@PathVariable String userId) throws ResourceNotFoundException {
        FavouritesRecipeResponse favouritesResponse = favouritesService.getFavoriteRecipes(userId);
        return ResponseEntity.ok(favouritesResponse);
    }

}
