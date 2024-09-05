package com.user.UserService.controller;

import com.user.UserService.Entity.CuisineDTO;
import com.user.UserService.RecipeSeviceClient.RecipeServiceClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins/cuisines")
public class AdminCuisineController {

    @Autowired
    private RecipeServiceClient recipeServiceClient;
    public AdminCuisineController(RecipeServiceClient recipeServiceClient) {
        this.recipeServiceClient = recipeServiceClient;
    }

    @GetMapping("/view")
    public ResponseEntity<List<CuisineDTO>> getAllCuisines() {
        try {
            List<CuisineDTO> allCuisines = recipeServiceClient.getAllCuisines();
            return ResponseEntity.ok(allCuisines);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.status())).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<CuisineDTO> saveCuisine(@RequestBody CuisineDTO cuisineDTO) {
        try {
            CuisineDTO addedCuisine = recipeServiceClient.addCuisine(cuisineDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedCuisine);
        } catch (FeignException.Conflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.status())).build();
        }
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<CuisineDTO>> fetchEnabledCuisines() {
        List<CuisineDTO> cuisines = recipeServiceClient.getEnabledCuisines();
        return ResponseEntity.ok(cuisines);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Void> disableCuisine(@PathVariable Long id) {
        try {
            recipeServiceClient.disableCuisine(id);
            return ResponseEntity.ok().build();
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.status())).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuisine(@PathVariable Long id) {
        try {
            recipeServiceClient.deleteCuisine(id);
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.status())).build();
        }
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enableCuisine(@PathVariable Long id) {

        try {
            recipeServiceClient.enableCuisine(id);
            return ResponseEntity.ok().build();
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.status())).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuisineDTO> updateCuisine(@PathVariable Long id, @RequestBody CuisineDTO cuisineDTO) {
        try {
            CuisineDTO updatedCuisine = recipeServiceClient.updateCuisine(id, cuisineDTO);
            return ResponseEntity.ok(updatedCuisine);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.status())).build();
        }
    }


}