package com.recipe.recipeservice.repository;

import com.recipe.recipeservice.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    @Query("SELECT r FROM Recipe r " +
            "LEFT JOIN r.category c " +
            "LEFT JOIN r.cuisine cu " +
            "LEFT JOIN r.tags t " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.dietaryRestrictions) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(cu.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.difficultyLevel) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.dietaryRestrictions) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Recipe> findByKeyword(@Param("keyword") String keyword);
}
