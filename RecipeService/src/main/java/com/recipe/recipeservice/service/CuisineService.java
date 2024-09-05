package com.recipe.recipeservice.service;

import com.recipe.recipeservice.entity.Cuisine;
import com.recipe.recipeservice.handlers.CuisineNotFoundException;
import com.recipe.recipeservice.repository.CuisineRepository;
import org.springframework.stereotype.Service;

@Service
public class CuisineService {

    private CuisineRepository cuisineRepository;
    public CuisineService(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

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
    public boolean doesCuisineExistByName(String name) {
        return cuisineRepository.findByName(name).isPresent();
    }

    public boolean doesCuisineExistById(Long id) {
        return cuisineRepository.findById(id).isPresent();
    }


}