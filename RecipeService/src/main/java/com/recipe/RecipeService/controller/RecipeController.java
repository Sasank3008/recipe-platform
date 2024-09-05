package com.recipe.RecipeService.controller;

import com.recipe.RecipeService.Entity.Cuisine;
import com.recipe.RecipeService.Entity.CuisineDTO;
import com.recipe.RecipeService.Model.CuisineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private CuisineRepository cuisineRepository;

    private List<CuisineDTO> convertToDtoList(List<Cuisine> cuisines) {
        return cuisines.stream()
                .map(cuisine -> new CuisineDTO(cuisine.getId(), cuisine.getName(), cuisine.isEnabled()))
                .collect(Collectors.toList());
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
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/cuisines/{id}")
    public ResponseEntity<Void> deleteCuisine(@PathVariable Long id) {
        return cuisineRepository.findById(id)
                .map(cuisine -> {
                    cuisineRepository.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                }).orElseGet(() -> new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/cuisines/enable/{id}")
    public ResponseEntity<Void> enableCuisine(@PathVariable Long id) {
        return cuisineRepository.findById(id)
                .map(cuisine -> {
                    cuisine.setEnabled(true);
                    cuisineRepository.save(cuisine);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
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
}