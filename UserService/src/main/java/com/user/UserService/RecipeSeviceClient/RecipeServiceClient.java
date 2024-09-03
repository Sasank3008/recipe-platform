package com.user.UserService.RecipeSeviceClient;

import com.user.UserService.Entity.CuisineDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "recipe-service", path = "/api/recipes")
public interface RecipeServiceClient {

    @PostMapping("/cuisines")
    CuisineDTO addCuisine(@RequestBody CuisineDTO cuisineDTO);

    @GetMapping("/cuisines/enabled")
    List<CuisineDTO> getEnabledCuisines();

    @PutMapping("/cuisines/disable/{id}")
    void disableCuisine(@PathVariable("id") Long id);

    @DeleteMapping("/cuisines/{id}")
    void deleteCuisine(@PathVariable("id") Long id);

    @PutMapping("/cuisines/enable/{id}")
    void enableCuisine(@PathVariable("id") Long id);

    @PutMapping("/cuisines/{id}")
    CuisineDTO updateCuisine(@PathVariable("id") Long id, @RequestBody CuisineDTO cuisineDTO);
}