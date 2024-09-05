package com.recipe.recipeservice.model;

import com.recipe.recipeservice.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
    Optional<Cuisine> findByName(String name);  // Removed the isDeleted condition
    List<Cuisine> findByIsEnabled(boolean isEnabled);  // Removed the isDeleted condition
    Optional<Cuisine> findById(Long id);
    @Query("SELECT c FROM Cuisine c")
    List<Cuisine> getAllCuisines();
}