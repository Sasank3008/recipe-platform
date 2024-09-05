package com.user.userservice.controller;

import com.user.userservice.dto.ApiResponse;
import com.user.userservice.dto.CuisineDTO;
import com.user.userservice.cuisineserviceclient.RecipeServiceClient;
import com.user.userservice.exception.CuisineIdNotFoundException;
import com.user.userservice.exception.DuplicateCuisineException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/admins/cuisines")
public class AdminCuisineController {

    private static final String CUISINE_NOT_FOUND_MESSAGE = "Cuisine not found with id: ";
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
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistByName(cuisineDTO.getName());
        Boolean doesExist = responseEntity.getBody();

        if (Boolean.TRUE.equals(doesExist)) {
            throw new DuplicateCuisineException("A cuisine with the name '" + cuisineDTO.getName() + "' already exists.");
        }


            CuisineDTO addedCuisine = recipeServiceClient.addCuisine(cuisineDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedCuisine);
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<CuisineDTO>> fetchEnabledCuisines() {
        List<CuisineDTO> cuisines = recipeServiceClient.getEnabledCuisines();
        return ResponseEntity.ok(cuisines);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<ApiResponse> disableCuisine(@PathVariable Long id) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistById(id);
        Boolean doesExist = responseEntity.getBody();
        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }
        recipeServiceClient.disableCuisine(id);
        ApiResponse response=ApiResponse.builder()
                .response("Cuisine disabled Succesfully")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCuisine(@PathVariable Long id) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistById(id);
        Boolean doesExist = responseEntity.getBody();
        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }
            recipeServiceClient.deleteCuisine(id);
        ApiResponse response=ApiResponse.builder()
                .response("Cuisine deleted Succesfully")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);


    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<ApiResponse> enableCuisine(@PathVariable Long id) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistById(id);
        Boolean doesExist = responseEntity.getBody();
        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }
            recipeServiceClient.enableCuisine(id);
        ApiResponse response=ApiResponse.builder()
                .response("Cuisine enabled Succesfully")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CuisineDTO> updateCuisine(@PathVariable Long id, @RequestBody CuisineDTO cuisineDTO) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistById(id);
        Boolean doesExist = responseEntity.getBody();

        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }  CuisineDTO updatedCuisine = recipeServiceClient.updateCuisine(id, cuisineDTO);
        return ResponseEntity.ok(updatedCuisine);
    }


}