package com.user.userservice.cuisineserviceclient;

import com.user.userservice.dto.CuisineDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/cuisines")
    List<CuisineDTO> getAllCuisines();
    @GetMapping("/cuisines/exist/by-id")
    public ResponseEntity<Boolean> doesCuisineExistById(@RequestParam Long id);
    @GetMapping("/cuisines/exist/by-name")
    public ResponseEntity<Boolean> doesCuisineExistByName(@RequestParam String name);
}