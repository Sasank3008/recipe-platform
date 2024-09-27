package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.dto.*;
import com.recipe.recipeservice.entity.Tag;
import com.recipe.recipeservice.entity.Category;
import com.recipe.recipeservice.exception.InvalidInputException;
import com.recipe.recipeservice.repository.CuisineRepository;
import com.recipe.recipeservice.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/recipes")
@RequiredArgsConstructor
public class RecipeAddController {
    private final RecipeService recipeService;
    private final CuisineRepository cuisineRepository;
    private final ModelMapper modelMapper;
    @Value("${project.image}")
    String path;
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> addRecipe(@ModelAttribute @Valid AddRecipeDTO addRecipeDto) throws InvalidInputException, IOException, MethodArgumentNotValidException {
        recipeService.createRecipe(addRecipeDto);
        ApiResponse response = ApiResponse.builder()
                .response("Successfully Added the Recipe")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(recipeService.getAllTags());
    }
    @PostMapping("/tags")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        return ResponseEntity.ok(recipeService.createTag(tag));
    }
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(recipeService.getAllCategories());
    }
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(recipeService.createCategory(category));
    }

    @PutMapping("{id}/status/{status}")
    public ResponseEntity<SuccessResponse> editRecipeStatus(@PathVariable("id") String id, @PathVariable("status") String status) throws InvalidInputException {
        return recipeService.editRecipeStatus(id, status);
    }

    @GetMapping("recipes/cuisines")
    public ResponseEntity<CuisineFilterListDTO> fetchAllCuisines() {
        CuisineFilterListDTO cuisineFilterListDTO = recipeService.fetchAllCuisines();
        return ResponseEntity.ok(cuisineFilterListDTO);
    }

    @GetMapping("recipes/categories")
    public ResponseEntity<CategoryFilterListDTO> fetchAllCategory() {
        CategoryFilterListDTO categoryFilterListDTO = recipeService.fetchAllCategory();
        return ResponseEntity.ok(categoryFilterListDTO);
    }

    @GetMapping("admins/filter")
    public ResponseEntity<RecipeFilterListDTO> fetchAllRecipesByFilters(
            @RequestParam(required = false) Long cuisineId,
            @RequestParam(required = false) Long categoryId
    ) throws InvalidInputException {
        RecipeFilterListDTO recipeFilterListDTO = recipeService.fetchAllRecipesByTwoFilters(cuisineId, categoryId);

        return ResponseEntity.ok(recipeFilterListDTO);
    }

}
