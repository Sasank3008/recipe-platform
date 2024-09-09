package com.user.userservice.controller;

import com.user.userservice.dto.ApiResponse;
import com.user.userservice.dto.CuisineDTO;
import com.user.userservice.cuisineserviceclient.RecipeServiceClient;
import com.user.userservice.exception.CuisineIdNotFoundException;
import com.user.userservice.exception.DuplicateCuisineException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admins")
public class AdminController {

    private static final String CUISINE_NOT_FOUND_MESSAGE = "Cuisine not found with id: ";
    private final RecipeServiceClient recipeServiceClient;

    @GetMapping("/cuisines")
    public ResponseEntity<List<CuisineDTO>> getAllCuisines() {
        return handleFeignClientCall(recipeServiceClient::getAllCuisines);
    }

    @PostMapping("/cuisines")
    public ResponseEntity<CuisineDTO> saveCuisine(@RequestBody CuisineDTO cuisineDTO) {
        checkCuisineNameExists(cuisineDTO.getName());
        CuisineDTO addedCuisine = recipeServiceClient.addCuisine(cuisineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCuisine);
    }

    @GetMapping("/cuisines/enabled")
    public ResponseEntity<List<CuisineDTO>> fetchEnabledCuisines() {
        return ResponseEntity.ok(recipeServiceClient.getEnabledCuisines());
    }
    @DeleteMapping("/cuisines/{id}")
    public ResponseEntity<ApiResponse> deleteCuisine(@PathVariable Long id) {
        checkCuisineExistsById(id);
        recipeServiceClient.deleteCuisine(id);
        return buildApiResponse("Cuisine deleted successfully", HttpStatus.OK);
    }
    @PutMapping("/cuisines/{id}")
    public ResponseEntity<CuisineDTO> updateCuisine(@PathVariable Long id, @RequestBody CuisineDTO cuisineDTO) {
        checkCuisineExistsById(id);
        CuisineDTO updatedCuisine = recipeServiceClient.updateCuisine(id, cuisineDTO);
        return ResponseEntity.ok(updatedCuisine);
    }

    private void checkCuisineExistsById(Long id) {
        Boolean doesExist = recipeServiceClient.doesCuisineExistById(id).getBody();
        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }
    }

    private void checkCuisineNameExists(String name) {
        Boolean doesExist = recipeServiceClient.doesCuisineExistByName(name).getBody();
        if (Boolean.TRUE.equals(doesExist)) {
            throw new DuplicateCuisineException("A cuisine with the name '" + name + "' already exists.");
        }
    }

    private ResponseEntity<ApiResponse> buildApiResponse(String message, HttpStatus status) {
        ApiResponse response = ApiResponse.builder()
                .response(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(response);
    }

    private <T> ResponseEntity<T> handleFeignClientCall(Supplier<T> supplier) {
        try {
            T result = supplier.get();
            return ResponseEntity.ok(result);
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            return ResponseEntity.status(status != null ? status : HttpStatus.BAD_GATEWAY).build();
        }
    }
    @PutMapping("/toggle-cuisine/{id}")
    public ResponseEntity<ApiResponse> toggleCuisineEnabled(@PathVariable Long id) {
        checkCuisineExistsById(id);
        Boolean isEnabledWrapper = recipeServiceClient.isCuisineEnabled(id);
        boolean isCurrentlyEnabled = Boolean.TRUE.equals(isEnabledWrapper);
        if (isCurrentlyEnabled) {
            recipeServiceClient.disableCuisine(id);
            return buildApiResponse("Cuisine disabled successfully", HttpStatus.OK);
        } else {
            recipeServiceClient.enableCuisine(id);
            return buildApiResponse("Cuisine enabled successfully", HttpStatus.OK);
        }
    }

}