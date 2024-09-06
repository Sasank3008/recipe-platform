package com.recipe.RecipeService.Service;

import com.recipe.RecipeService.Entity.Cuisine;
import com.recipe.RecipeService.Handlers.CuisineNotFoundException;
import com.recipe.RecipeService.Handlers.DuplicateCuisineException;
import com.recipe.RecipeService.Model.CuisineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CuisineService {
    @Autowired
    private CuisineRepository cuisineRepository;

    private Cuisine getCuisineById(Long id) {
        return cuisineRepository.findById(id)
                .orElseThrow(() -> new CuisineNotFoundException("Cuisine not found with id: " + id));
    }
    public void disableCuisineById(Long id) {
        Cuisine cuisine = getCuisineById(id);
        cuisine.setEnabled(false);
        cuisineRepository.save(cuisine);
    }

    public void deleteCuisineById(Long id) {
        Cuisine cuisine = getCuisineById(id);
        cuisineRepository.deleteById(id);
    }

    public void enableCuisineById(Long id) {
        Cuisine cuisine = getCuisineById(id);
        cuisine.setEnabled(true);
        cuisineRepository.save(cuisine);
    }

    public Cuisine updateCuisineById(Long id, Cuisine updatedDetails) {
        Cuisine cuisine = getCuisineById(id);
        cuisine.setName(updatedDetails.getName());
        cuisine.setEnabled(updatedDetails.isEnabled());
        return cuisineRepository.save(cuisine);
    }

}