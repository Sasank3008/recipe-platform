package com.recipe.recipeservice.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRecipeDTO {
    private String id;
    @NotBlank(message = "Please Enter Valid name of the recipe")
    private String name;
    @NotBlank(message = "ingredients must not be blank or empty")
    private String ingredients;
    @NotBlank(message = "description must not be empty or null")
    private String description;
    @NotBlank
    private String categoryId;
    @NotBlank
    private String cuisineId;
    @NotBlank
    private String cookingTime;
    private MultipartFile file;
    private List<String> tagIds;
    @NotBlank
    private String difficultyLevel;
    @NotBlank
    private String dietaryRestrictions;

}
