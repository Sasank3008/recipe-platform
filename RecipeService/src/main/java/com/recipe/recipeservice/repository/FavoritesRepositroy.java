package com.recipe.recipeservice.repository;
import com.recipe.recipeservice.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    Optional<Favorites> findByUserId(Long userId);
}
