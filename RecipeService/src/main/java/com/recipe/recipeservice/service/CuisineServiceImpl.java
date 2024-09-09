package com.recipe.recipeservice.service;

import com.recipe.recipeservice.entity.Cuisine;

public interface CuisineServiceImpl {
        void disableCuisineById(Long id);
        void deleteCuisineById(Long id);
        void enableCuisineById(Long id);
        Cuisine updateCuisineById(Long id, Cuisine updatedDetails);
        boolean doesCuisineExistByName(String name);
        boolean doesCuisineExistById(Long id);

    }