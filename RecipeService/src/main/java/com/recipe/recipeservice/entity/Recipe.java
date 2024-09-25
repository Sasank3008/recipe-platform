package com.recipe.recipeservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String ingredients;
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "cuisine_id")
    private Cuisine cuisine;
    private int cookingTime;
    private String imageUrl;
    @ManyToMany
    @JoinTable(
            name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
    @Enumerated(EnumType.STRING)
    private  Status status;
    private String dietaryRestrictions;
    @Column(name="userId")
    private String user;
}
