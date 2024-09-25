package com.recipe.recipeservice.service;


import com.recipe.recipeservice.dto.AddRecipeDTO;
import com.recipe.recipeservice.dto.UpdateRecipeDTO;
import com.recipe.recipeservice.dto.ViewRecipeDTO;
import com.recipe.recipeservice.entity.Category;
import com.recipe.recipeservice.entity.Cuisine;
import com.recipe.recipeservice.entity.Recipe;
import com.recipe.recipeservice.entity.Tag;
import com.recipe.recipeservice.exception.IdNotFoundException;
import com.recipe.recipeservice.exception.InvalidInputException;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
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
    ViewRecipeDTO getRecipe(Long id) throws ResourceNotFoundException;
    public byte[] getRecipeProfileImage(Long userId) throws IOException, ResourceNotFoundException;
    void updateRecipe(UpdateRecipeDTO updateRecipeDTO, Long id) throws IdNotFoundException,IOException;
    String updateRecipeImage(String path, MultipartFile file) throws IdNotFoundException, IOException;


}
