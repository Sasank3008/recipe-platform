package com.user.userservice.controller;

import com.user.userservice.entity.CuisineDTO;
import com.user.userservice.cuisineserviceclient.RecipeServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/admins/cuisines")
public class AdminCuisineController {


    private RecipeServiceClient recipeServiceClient;
    @Autowired
    public AdminCuisineController(RecipeServiceClient recipeServiceClient) {
        this.recipeServiceClient = recipeServiceClient;
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<CuisineDTO>> getAllCuisines() {
        try {
            List<CuisineDTO> allCuisines = recipeServiceClient.getAllCuisines();
            return ResponseEntity.ok(allCuisines);
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            if (status == null) {
                // Assuming BAD_GATEWAY as a fallback status, but you can choose a different one
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<CuisineDTO> saveCuisine(@RequestBody CuisineDTO cuisineDTO) {
        try {
            CuisineDTO addedCuisine = recipeServiceClient.addCuisine(cuisineDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedCuisine);
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            if (status == null) {
                // Assuming BAD_GATEWAY as a fallback status, but you can choose a different one
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status).build();
        }
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<CuisineDTO>> fetchEnabledCuisines() {
        List<CuisineDTO> cuisines = recipeServiceClient.getEnabledCuisines();
        return ResponseEntity.ok(cuisines);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableCuisine(@PathVariable Long id) {
        try {
            recipeServiceClient.disableCuisine(id);
            return ResponseEntity.ok().build();
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            if (status == null) {
                // Assuming BAD_GATEWAY as a fallback status, but you can choose a different one
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuisine(@PathVariable Long id) {
        try {
            recipeServiceClient.deleteCuisine(id);
            return ResponseEntity.noContent().build();
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            if (status == null) {
                // Assuming BAD_GATEWAY as a fallback status, but you can choose a different one
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status).build();
        }
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableCuisine(@PathVariable Long id) {

        try {
            recipeServiceClient.enableCuisine(id);
            return ResponseEntity.ok().build();
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            if (status == null) {
                // Assuming BAD_GATEWAY as a fallback status, but you can choose a different one
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuisineDTO> updateCuisine(@PathVariable Long id, @RequestBody CuisineDTO cuisineDTO) {
        try {
            CuisineDTO updatedCuisine = recipeServiceClient.updateCuisine(id, cuisineDTO);
            return ResponseEntity.ok(updatedCuisine);
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            if (status == null) {
                // Assuming BAD_GATEWAY as a fallback status, but you can choose a different one
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status).build();
        }
    }


}