package com.user.userservice.service;

import com.user.userservice.feignclient.RecipeServiceClient;
import com.user.userservice.dto.CuisineDTO;
import com.user.userservice.exception.CuisineIdNotFoundException;
import com.user.userservice.exception.DuplicateCuisineException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor

public class CuisineServiceImpls implements CuisineService {

    private final RecipeServiceClient recipeServiceClient;
    @Value("${project.image}")
    public String path;

    private static final String CUISINE_NOT_FOUND = "Cuisine not found with id: ";
    private static final String INVALID_FILE_TYPE = "Invalid file type. Only PNG, JPG, JPEG, and SVG are allowed.";
    private static final String FILE_MUST_NOT_BE_NULL = "File must not be null or empty.";

    @Override
    public List<CuisineDTO> getAllCuisines() {
        return recipeServiceClient.getAllCuisines();
    }

    @Override
    public CuisineDTO addCuisine(String name, boolean isEnabled, MultipartFile file) {
        if (checkCuisineNameExists(name)) {
            throw new DuplicateCuisineException("A cuisine with the name '" + name + "' already exists.");
        }
        String imageUrl = saveImage(file);
        CuisineDTO cuisine = new CuisineDTO(null, name, isEnabled, imageUrl);
        return recipeServiceClient.addCuisine(cuisine);
    }

    @Override
    public List<CuisineDTO> getEnabledCuisines() {
        return recipeServiceClient.getEnabledCuisines();
    }

    @Override
    public void deleteCuisine(Long id) {
        if (!checkCuisineExistsById(id)) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND + id);
        }
        recipeServiceClient.deleteCuisine(id);
    }

    @Override
    public CuisineDTO updateCuisine(Long id, String name, boolean isEnabled, MultipartFile file) {
        if (!checkCuisineExistsById(id)) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND + id);
        }
        String imageUrl = saveImage(file);
        CuisineDTO cuisine = new CuisineDTO(id, name, isEnabled, imageUrl);
        return recipeServiceClient.updateCuisine(id, cuisine);
    }

    @Override
    public String toggleCuisineEnabled(Long id) {
        if (!checkCuisineExistsById(id)) {
            throw new CuisineIdNotFoundException(CUISINE_NOT_FOUND + id);
        }
        Boolean isEnabled = recipeServiceClient.isCuisineEnabled(id);
        if (isEnabled == null) {
            throw new IllegalStateException("Cuisine enabled status is undefined for id: " + id);
        }
        if (isEnabled) {
            recipeServiceClient.disableCuisine(id);
            return "Cuisine disabled successfully";
        } else {
            recipeServiceClient.enableCuisine(id);
            return "Cuisine enabled successfully";
        }
    }

    @Override
    public boolean checkCuisineExistsById(Long id) {
        ResponseEntity<Boolean> response = recipeServiceClient.doesCuisineExistById(id);
        Boolean exists = response != null ? response.getBody() : null;
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public boolean checkCuisineNameExists(String name) {
        ResponseEntity<Boolean> response = recipeServiceClient.doesCuisineExistByName(name);
        Boolean exists = response != null ? response.getBody() : null;
        return Boolean.TRUE.equals(exists);
    }

    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(FILE_MUST_NOT_BE_NULL);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File must have a valid name.");
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(".png", ".jpg", ".jpeg", ".svg").contains(fileExtension)) {
            throw new IllegalArgumentException(INVALID_FILE_TYPE);
        }

        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueIdentifier = UUID.randomUUID().toString();
        String newFileName = uniqueIdentifier + fileExtension;

        try {
            Files.copy(file.getInputStream(), Paths.get(path, newFileName));
            return newFileName;
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to store file due to IO Exception", e);
        }
    }
}