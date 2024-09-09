package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.entity.Cuisine;
import com.recipe.recipeservice.dto.CuisineDTO;
import com.recipe.recipeservice.repository.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {


    private CuisineRepository cuisineRepository;

    @Autowired
    public RecipeController(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    private List<CuisineDTO> convertToDtoList(List<Cuisine> cuisines) {
        return cuisines.stream()
                .map(cuisine -> new CuisineDTO(cuisine.getId(), cuisine.getName(), cuisine.isEnabled()))
                .toList();
    }
    private ResponseEntity<Void> responseEntityOk() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<Void> responseEntityNotFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/cuisines")
    public ResponseEntity<CuisineDTO> addCuisine(@RequestBody CuisineDTO cuisineDTO) {
        Optional<Cuisine> existingCuisine = cuisineRepository.findByName(cuisineDTO.getName());
        if (existingCuisine.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Cuisine newCuisine = new Cuisine();
        newCuisine.setName(cuisineDTO.getName());
        newCuisine.setEnabled(true);

        Cuisine addedCuisine = cuisineRepository.save(newCuisine);
        CuisineDTO responseDTO = new CuisineDTO(addedCuisine.getId(), addedCuisine.getName(), addedCuisine.isEnabled());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/cuisines/enabled")
    public ResponseEntity<List<CuisineDTO>> getEnabledCuisines() {
        List<Cuisine> cuisines = cuisineRepository.findByIsEnabled(true);
        List<CuisineDTO> cuisineDTOs = convertToDtoList(cuisines);
        return ResponseEntity.ok(cuisineDTOs);
    }

    @PutMapping("/cuisines/disable/{id}")
    public ResponseEntity<Void> disableCuisine(@PathVariable Long id) {
        return cuisineRepository.findById(id)
                .map(cuisine -> {
                    cuisine.setEnabled(false);
                    cuisineRepository.save(cuisine);
                    return responseEntityOk();  // Use a helper method
                }).orElseGet(this::responseEntityNotFound);
    }

    @DeleteMapping("/cuisines/{id}")
    public ResponseEntity<Void> deleteCuisine(@PathVariable Long id) {
        return cuisineRepository.findById(id)
                .map(cuisine -> {
                    cuisineRepository.deleteById(id);
                    return responseEntityOk();  // Use a helper method
                }).orElseGet(this::responseEntityNotFound);
    }

    @PutMapping("/cuisines/enable/{id}")
    public ResponseEntity<Void> enableCuisine(@PathVariable Long id) {
        return cuisineRepository.findById(id)
                .map(cuisine -> {
                    cuisine.setEnabled(true);
                    cuisineRepository.save(cuisine);
                    return responseEntityOk();  // Use a helper method
                }).orElseGet(this::responseEntityNotFound);
    }

    @PutMapping("/cuisines/{id}")
    public ResponseEntity<CuisineDTO> updateCuisine(@PathVariable Long id, @RequestBody CuisineDTO cuisineDTO) {
        return cuisineRepository.findById(id)
                .map(cuisine -> {
                    cuisine.setName(cuisineDTO.getName());
                    cuisine.setEnabled(cuisineDTO.isEnabled());
                    cuisineRepository.save(cuisine);
                    CuisineDTO responseDTO = new CuisineDTO(cuisine.getId(), cuisine.getName(), cuisine.isEnabled());
                    return ResponseEntity.ok(responseDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cuisines")
    public ResponseEntity<List<CuisineDTO>> getAllCuisines() {
        List<Cuisine> cuisines = cuisineRepository.getAllCuisines();
        List<CuisineDTO> cuisineDTOs = convertToDtoList(cuisines);
        return ResponseEntity.ok(cuisineDTOs);
    }
    @GetMapping("/cuisines/exist/by-name")
    public ResponseEntity<Boolean> doesCuisineExistByName(@RequestParam String name) {
        boolean exists = cuisineRepository.doesCuisineExistByName(name);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/cuisines/exist/by-id")
    public ResponseEntity<Boolean> doesCuisineExistById(@RequestParam Long id) {
        boolean exists = cuisineRepository.doesCuisineExistById(id);
        return ResponseEntity.ok(exists);
    }
}