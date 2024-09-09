package com.user.userservice.controller;

import com.user.userservice.dto.ApiResponse;
import com.user.userservice.dto.CountryDTO;
import com.user.userservice.dto.CuisineDTO;
import com.user.userservice.cuisineserviceclient.RecipeServiceClient;
import com.user.userservice.exception.CuisineIdNotFoundException;
import com.user.userservice.exception.DuplicateCuisineException;
import com.user.userservice.exception.CountryAlreadyExistsException;
import com.user.userservice.service.CountryService;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/admins")
public class AdminController {

    private static final String CUISINE_NOT_FOUND_MESSAGE = "Cuisine not found with id: ";
    private final RecipeServiceClient recipeServiceClient;
    private final   CountryService countryService;


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
        ApiResponse response=ApiResponse.builder()
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
        ApiResponse response=ApiResponse.builder()
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
        ApiResponse response=ApiResponse.builder()
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
        }CuisineDTO updatedCuisine = recipeServiceClient.updateCuisine(id, cuisineDTO);
        return ResponseEntity.ok(updatedCuisine);
    }
    @PostMapping("/countries")
    public ResponseEntity<CountryDTO> saveCountry(@RequestBody @Valid CountryDTO countryDTO) throws MethodArgumentNotValidException, CountryAlreadyExistsException {

        CountryDTO savedCountry = countryService.saveCountry(countryDTO);
        return ResponseEntity.ok().body(savedCountry);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<CountryDTO>> fetchAllCountries() {
        List<CountryDTO> countries = countryService.fetchCountries();
        return ResponseEntity.ok().body(countries);
    }


}