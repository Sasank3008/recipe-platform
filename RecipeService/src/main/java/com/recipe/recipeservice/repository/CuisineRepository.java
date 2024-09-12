package com.recipe.recipeservice.repository;

import com.recipe.recipeservice.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
    Optional<Cuisine> findByName(String name);
    List<Cuisine> findByIsEnabled(boolean isEnabled);
    @Query("SELECT c FROM Cuisine c")
    List<Cuisine> getAllCuisines();
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cuisine c WHERE c.id = :id")
    boolean existsById(Long id);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cuisine c WHERE c.name = :name")
    boolean existsByName(String name);
}