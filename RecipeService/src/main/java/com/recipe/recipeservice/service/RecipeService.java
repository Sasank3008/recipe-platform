package com.recipe.recipeservice.service;


import com.recipe.recipeservice.dto.AddRecipeDTO;
import com.recipe.recipeservice.entity.*;
import com.recipe.recipeservice.exception.InvalidInputException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface RecipeService {

    public List<Tag> getAllTags();
    public Tag createTag(Tag tag);
    public List<Category> getAllCategories();
    public Category createCategory(Category category);
    public Cuisine createCuisine(Cuisine cuisine);
    public Recipe createRecipe(AddRecipeDTO addRecipeDTO) throws InvalidInputException, IOException, MethodArgumentNotValidException;
    public Recipe mapRecipeDTOtoRecipe(AddRecipeDTO addRecipeDTO) throws IOException, InvalidInputException;
    public String uploadImage(String path, MultipartFile file) throws IOException, NullPointerException, InvalidInputException;

}