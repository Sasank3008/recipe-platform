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

    public Cuisine addOrUpdateCuisine(Cuisine cuisine) throws DuplicateCuisineException {
        // Check if the cuisine with the same name already exists
        Optional<Cuisine> existingCuisine = cuisineRepository.findByName(cuisine.getName());
        if (existingCuisine.isPresent()) {
            throw new DuplicateCuisineException("Cuisine with name " + cuisine.getName() + " already exists.");
        }
        return cuisineRepository.save(cuisine);
    }

    public List<Cuisine> getEnabledCuisines() {
        return cuisineRepository.findByIsEnabled(true);
    }

    public void disableCuisineById(Long id) {
        Cuisine cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new CuisineNotFoundException("Cuisine not found with id: " + id));
        cuisine.setEnabled(false);
        cuisineRepository.save(cuisine);
    }

    public void deleteCuisineById(Long id) {
        Cuisine cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new CuisineNotFoundException("Cuisine not found with id: " + id));
        cuisineRepository.deleteById(id);
    }

    public void enableCuisineById(Long id) {
        System.out.println("im in enable iod");
        Cuisine cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new CuisineNotFoundException("Cuisine not found with id: " + id));
        cuisine.setEnabled(true);
        cuisineRepository.save(cuisine);
    }

    public Cuisine updateCuisineById(Long id, Cuisine updatedDetails) {
        return cuisineRepository.findById(id)
                .map(cuisine -> {
                    cuisine.setName(updatedDetails.getName());
                    cuisine.setEnabled(updatedDetails.isEnabled());
                    return cuisineRepository.save(cuisine);
                }).orElseThrow(() -> new CuisineNotFoundException("Cuisine not found with id: " + id));
    }

    public List<Cuisine>  getAllCuisines() {
        return cuisineRepository.findAll();
    }
}