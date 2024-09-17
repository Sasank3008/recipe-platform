package com.recipe.recipeservice.entity;

import lombok.Getter;

@Getter
public enum DifficultyLevel {
    EASY(1),
    MEDIUM(2),
    HARD(3);
    private final int id;

    DifficultyLevel(int id) {
        this.id = id;
    }

    public static DifficultyLevel valueOf(int id) {
        for (DifficultyLevel level : values()) {
            if (level.getId() == id) {
                return level;
            }
        }

        throw new IllegalArgumentException("Invalid DifficultyLevel id: " + id);
    }
}
