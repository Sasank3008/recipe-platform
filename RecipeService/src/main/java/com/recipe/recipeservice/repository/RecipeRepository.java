package com.recipe.recipeservice.repository;

import com.recipe.recipeservice.entity.DifficultyLevel;
import com.recipe.recipeservice.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    @Query("SELECT r FROM Recipe r WHERE " +
            "(:cuisineId IS NULL OR r.cuisine.id = :cuisineId) AND " +
            "(:categoryId IS NULL OR r.category.id = :categoryId) AND " +
            "(:cookingTime IS NULL OR r.cookingTime <= :cookingTime) AND " +
            "(:difficulty IS NULL OR r.difficultyLevel = :difficulty)")
    List<Recipe> findRecipesByFilters(
            @Param("cuisineId") Long cuisineId,
            @Param("categoryId") Long categoryId,
            @Param("cookingTime") Integer cookingTime,
            @Param("difficulty") DifficultyLevel difficulty);
}
