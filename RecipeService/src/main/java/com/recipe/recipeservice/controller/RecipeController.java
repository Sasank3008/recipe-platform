package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.constants.ErrorConstants;
import com.recipe.recipeservice.dto.CuisineDTO;
import com.recipe.recipeservice.dto.CuisineResponse;
import com.recipe.recipeservice.dto.RecipeListDTO;
import com.recipe.recipeservice.dto.RecipeDTO;
import com.recipe.recipeservice.entity.Category;
import com.recipe.recipeservice.exception.InvalidInputException;
import com.recipe.recipeservice.service.CuisineService;
import com.recipe.recipeservice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final CuisineService cuisineService;
    @GetMapping("/cuisines/enabled")
    public ResponseEntity<CuisineResponse> getEnabledCuisines() {
        List<CuisineDTO> cuisineDTOs = cuisineService.getEnabledCuisines();
        CuisineResponse response = CuisineResponse.builder()
                .cuisines(cuisineDTOs)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(recipeService.getAllCategories());
    }

    @GetMapping("/search")
    public ResponseEntity<RecipeListDTO> searchRecipes(@RequestParam String keyword) {
        keyword = keyword.trim().replaceAll("\\s+", " ");
        List<RecipeDTO> recipes = recipeService.searchRecipes(keyword);
        RecipeListDTO response = RecipeListDTO.builder()
                .recipeList(recipes)
                .status(HttpStatus.OK.toString())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<RecipeListDTO> fetchAllRecipesByFilters(
            @RequestParam(required = false) Long cuisineId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer cookingTime,
            @RequestParam(required = false) String difficulty) throws InvalidInputException {
        if((cuisineId!=null && cuisineId<0) || (categoryId!=null && categoryId<0) || (cookingTime!=null && cookingTime<0))
            throw new InvalidInputException(ErrorConstants.INVALID_INPUTS);
        RecipeListDTO recipesFiltered = RecipeListDTO.builder()
                .recipeList(recipeService.fetchRecipesByFilters(cuisineId, categoryId, cookingTime, difficulty))
                .status(HttpStatus.OK.toString())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(recipesFiltered);
    }
}
