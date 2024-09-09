package com.recipe.recipeservice.service;

import com.recipe.recipeservice.entity.Cuisine;
import com.recipe.recipeservice.repository.CuisineRepository;
import org.springframework.stereotype.Service;



@Service
public class CuisineService implements CuisineServiceImpl {

    private CuisineRepository cuisineRepository;
    public CuisineService(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    public void disableCuisineById(Long id) {
        cuisineRepository.findById(id).ifPresent(cuisine -> {
            cuisine.setEnabled(false);
            cuisineRepository.save(cuisine);
        });
    }

    public void deleteCuisineById(Long id) {
        cuisineRepository.deleteById(id);
    }

    public void enableCuisineById(Long id) {
        cuisineRepository.findById(id).ifPresent(cuisine -> {
            cuisine.setEnabled(true);
            cuisineRepository.save(cuisine);
        });
    }

    public Cuisine updateCuisineById(Long id, Cuisine updatedDetails) {
        return cuisineRepository.findById(id).map(cuisine -> {
            cuisine.setName(updatedDetails.getName());
            cuisine.setEnabled(updatedDetails.isEnabled());
            return cuisineRepository.save(cuisine);
        }).orElse(null);
    }

    public boolean doesCuisineExistByName(String name) {
        return cuisineRepository.findByName(name).isPresent();
    }

    public boolean doesCuisineExistById(Long id) {
        return cuisineRepository.findById(id).isPresent();
    }

    }