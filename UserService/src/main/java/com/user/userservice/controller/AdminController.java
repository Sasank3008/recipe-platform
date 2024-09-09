package com.user.userservice.controller;
import com.user.userservice.cuisineserviceclient.RecipeServiceClient;
import com.user.userservice.dto.AdminUserDTO;
import com.user.userservice.dto.ApiResponse;
import com.user.userservice.dto.CuisineDTO;
import com.user.userservice.exception.CuisineIdNotFoundException;
import com.user.userservice.exception.DuplicateCuisineException;
import com.user.userservice.exception.UserIdNotFoundException;
import com.user.userservice.service.AdminService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private static final String CUISINE_NOT_FOUND_MESSAGE = "Cuisine not found with id: ";
    private final RecipeServiceClient recipeServiceClient;

    private final AdminService adminService;
    @PutMapping("editUser/{id}")
    public ResponseEntity<AdminUserDTO> editUser(@PathVariable Long id, @RequestBody AdminUserDTO userDTO) throws UserIdNotFoundException, UserIdNotFoundException {
        AdminUserDTO updatedUserDTO = adminService.updateUser(id, userDTO);
        if (updatedUserDTO != null) {
            return ResponseEntity.ok(updatedUserDTO);
        } else {
            return ResponseEntity.notFound().build();

        }
    }


    @GetMapping("/cuisines")
    public ResponseEntity<List<CuisineDTO>> getAllCuisines() {
        try {
            List<CuisineDTO> allCuisines = recipeServiceClient.getAllCuisines();
            return ResponseEntity.ok(allCuisines);
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            if (status == null) {
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status).build();
        }
    }

    @PostMapping("/cuisines")
    public ResponseEntity<CuisineDTO> saveCuisine(@RequestBody CuisineDTO cuisineDTO) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistByName(cuisineDTO.getName());
        Boolean doesExist = responseEntity.getBody();

        if (Boolean.TRUE.equals(doesExist)) {
            throw new DuplicateCuisineException("A cuisine with the name '" + cuisineDTO.getName() + "' already exists.");
        }


        CuisineDTO addedCuisine = recipeServiceClient.addCuisine(cuisineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCuisine);
    }

    @GetMapping("/cuisines/enabled")
    public ResponseEntity<List<CuisineDTO>> fetchEnabledCuisines() {
        List<CuisineDTO> cuisines = recipeServiceClient.getEnabledCuisines();
        return ResponseEntity.ok(cuisines);
    }

    @PutMapping("/cuisines/{id}/disable")
    public ResponseEntity<ApiResponse> disableCuisine(@PathVariable Long id) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistById(id);
        Boolean doesExist = responseEntity.getBody();
        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }
        recipeServiceClient.disableCuisine(id);
        ApiResponse response = ApiResponse.builder()
                .response("Cuisine disabled Succesfully")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @DeleteMapping("/cuisines/{id}")
    public ResponseEntity<ApiResponse> deleteCuisine(@PathVariable Long id) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistById(id);
        Boolean doesExist = responseEntity.getBody();
        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }
        recipeServiceClient.deleteCuisine(id);
        ApiResponse response = ApiResponse.builder()
                .response("Cuisine deleted Succesfully")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);


    }

    @PutMapping("/cuisines/{id}/enable")
    public ResponseEntity<ApiResponse> enableCuisine(@PathVariable Long id) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistById(id);
        Boolean doesExist = responseEntity.getBody();
        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }
        recipeServiceClient.enableCuisine(id);
        ApiResponse response = ApiResponse.builder()
                .response("Cuisine enabled Succesfully")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/cuisines/{id}")
    public ResponseEntity<CuisineDTO> updateCuisine(@PathVariable Long id, @RequestBody CuisineDTO cuisineDTO) {
        ResponseEntity<Boolean> responseEntity = recipeServiceClient.doesCuisineExistById(id);
        Boolean doesExist = responseEntity.getBody();

        if (doesExist == null || !doesExist) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND_MESSAGE + id);
        }
        CuisineDTO updatedCuisine = recipeServiceClient.updateCuisine(id, cuisineDTO);
        return ResponseEntity.ok(updatedCuisine);
    }


}
