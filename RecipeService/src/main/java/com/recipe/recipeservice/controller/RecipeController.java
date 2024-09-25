package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.dto.CuisineDTO;
import com.recipe.recipeservice.dto.CuisineResponse;
import com.recipe.recipeservice.service.CuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final CuisineService cuisineService;
    @GetMapping("/cuisines/enabled")
    public ResponseEntity<CuisineResponse> getEnabledCuisines() {
        List<CuisineDTO> cuisineDTOs = cuisineService.getEnabledCuisines();
        CuisineResponse response = CuisineResponse.builder()
                .cuisines(cuisineDTOs)
                .build();
        return ResponseEntity.ok(response);
    }


}
