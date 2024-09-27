package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.dto.*;
import com.recipe.recipeservice.entity.ReviewRating;
import com.recipe.recipeservice.service.CuisineService;
import com.recipe.recipeservice.service.RecipeService;
import com.recipe.recipeservice.service.ReviewRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final CuisineService cuisineService;
    @Autowired
    private ReviewRatingService reviewRatingService;

    @GetMapping("/cuisines/enabled")
    public ResponseEntity<CuisineResponse> getEnabledCuisines() {
        List<CuisineDTO> cuisineDTOs = cuisineService.getEnabledCuisines();
        CuisineResponse response = CuisineResponse.builder()
                .cuisines(cuisineDTOs)
                .build();
        return ResponseEntity.ok(response);
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
    @GetMapping("/comments/{recipeId}")
    public ResponseEntity<AllCommentsDTO> getAllComments(@PathVariable Long recipeId) {
        List<ReviewRating> comments = reviewRatingService.getAllReviews(recipeId);
        AllCommentsDTO allCommentsDTO = AllCommentsDTO.builder()
                .reviews(comments)
                .build();
        return new ResponseEntity<>(allCommentsDTO, HttpStatus.OK);
    }

}
