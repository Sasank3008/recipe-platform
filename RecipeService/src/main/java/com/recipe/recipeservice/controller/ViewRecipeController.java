package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.constants.ControllerConstants;
import com.recipe.recipeservice.dto.ViewRecipeDTO;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
import com.recipe.recipeservice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class ViewRecipeController {
    private final RecipeService recipeService;
        @GetMapping("{id}"+ ControllerConstants.VIEW_RECIPE_PATH)
    public ResponseEntity<ViewRecipeDTO> getRecipe(@PathVariable Long id) throws ResourceNotFoundException {
            ViewRecipeDTO recipe= recipeService.getRecipe(id);
            return ResponseEntity.ok(recipe);
        }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileImage(@PathVariable Long id) throws IOException, ResourceNotFoundException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(recipeService.getRecipeProfileImage(id));
    }
    }

