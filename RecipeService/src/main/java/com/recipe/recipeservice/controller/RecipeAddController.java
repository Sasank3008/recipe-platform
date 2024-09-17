package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.dto.ApiResponse;
import com.recipe.recipeservice.dto.AddRecipeDTO;
import com.recipe.recipeservice.dto.CuisineDTO;
import com.recipe.recipeservice.entity.Tag;
import com.recipe.recipeservice.entity.Category;
import com.recipe.recipeservice.entity.Cuisine;
import com.recipe.recipeservice.exception.InvalidInputException;
import com.recipe.recipeservice.repository.CuisineRepository;
import com.recipe.recipeservice.service.RecipeService;
import com.recipe.recipeservice.service.RecipeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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
    @GetMapping("/cuisines")
    public ResponseEntity<List<CuisineDTO>> getAllCuisines() {
        List<CuisineDTO>list=cuisineRepository.findAll().stream()
                .map(country -> modelMapper.map(country, CuisineDTO.class))
                .toList();
        return ResponseEntity.ok().body(list);
    }
    @PostMapping("/cuisines")
    public ResponseEntity<Cuisine> createCuisine(@RequestBody Cuisine cuisine) {
        return ResponseEntity.ok(recipeService.createCuisine(cuisine));
    }
}
