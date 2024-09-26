package com.recipe.recipeservice.service;

import com.recipe.recipeservice.constants.ErrorConstants;
import com.recipe.recipeservice.dto.AddRecipeDTO;
import com.recipe.recipeservice.dto.RecipeDTO;
import com.recipe.recipeservice.dto.ViewRecipeDTO;
import com.recipe.recipeservice.entity.Recipe;
import com.recipe.recipeservice.entity.Tag;
import com.recipe.recipeservice.entity.Category;
import com.recipe.recipeservice.entity.DifficultyLevel;
import com.recipe.recipeservice.entity.Cuisine;
import com.recipe.recipeservice.entity.Status;
import com.recipe.recipeservice.exception.InvalidInputException;
import com.recipe.recipeservice.exception.ResourceNotFoundException;
import com.recipe.recipeservice.repository.RecipeRepository;
import com.recipe.recipeservice.repository.CategoryRepository;
import com.recipe.recipeservice.repository.CuisineRepository;
import com.recipe.recipeservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    @Value("${project.image}")
    String path;
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final CuisineRepository cuisineRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
    public Cuisine createCuisine(Cuisine cuisine) {
        return cuisineRepository.save(cuisine);
    }
    public Recipe createRecipe(AddRecipeDTO addRecipeDTO) throws InvalidInputException, IOException , MethodArgumentNotValidException {
           modelMapper.map(addRecipeDTO, Recipe.class);
          return recipeRepository.save(mapRecipeDTOtoRecipe(addRecipeDTO));
    }
    public Recipe mapRecipeDTOtoRecipe(AddRecipeDTO addRecipeDTO) throws IOException, InvalidInputException {
        Recipe recipe = modelMapper.map(addRecipeDTO, Recipe.class);
        recipe.setStatus(Status.PENDING);
        recipe.setCategory(categoryRepository.findById(Long.parseLong(addRecipeDTO.getCategoryId())).orElse(null));
        recipe.setCuisine(cuisineRepository.findById(Long.parseLong(addRecipeDTO.getCuisineId())).orElse(null));
        recipe.setDifficultyLevel(DifficultyLevel.valueOf(Integer.parseInt(addRecipeDTO.getDifficultyLevel())));
        List<Tag> tags = tagRepository.findAllById(addRecipeDTO.getTagIds().stream().map(Long::parseLong).collect(Collectors.toList()));
        recipe.setTags(tags);
        recipe.setImageUrl(uploadImage(path, addRecipeDTO.getFile()));
        return recipe;
    }
    public String uploadImage(String path, MultipartFile file) throws IOException, NullPointerException, InvalidInputException {
        if (file == null || file.isEmpty()) {
            throw new InvalidInputException("File must not be null or empty.");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new InvalidInputException(ErrorConstants.INVALID_FILE_NAME);
        }
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
        if (!allowedExtensions.contains(fileExtension)) {
            throw new InvalidInputException(ErrorConstants.INVALID_FILE_TYPE);
        }
        String uniqueIdentifier = UUID.randomUUID().toString();
        String newFileName = uniqueIdentifier + fileExtension;
        String filePath=path+File.separator+newFileName;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return newFileName;
    }
    public byte[] getRecipeProfileImage(Long id) throws IOException, ResourceNotFoundException {
        Recipe user = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.RECIPE_ID_NOT_FOUND));
        String imageUrl = path + File.separator + user.getImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new FileNotFoundException("Image url doesn't exist");
        }
        Path imagePath = Paths.get(imageUrl);
        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("File not found");
        }
        return Files.readAllBytes(imagePath);
    }
    @Override
    public ViewRecipeDTO getRecipe(Long id) throws ResourceNotFoundException {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorConstants.RECIPE_ID_NOT_FOUND+" "+id));
        return new ViewRecipeDTO(
                recipe.getName(),
                recipe.getIngredients(),
                recipe.getDescription(),
                recipe.getCookingTime(),
                recipe.getCuisine().getName(),
                recipe.getCategory().getName(),
                recipe.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList()),
                recipe.getDifficultyLevel().name(),
                recipe.getDietaryRestrictions()
        );
    }

    public List<RecipeDTO> fetchRecipesByFilters(Long cuisineId, Long categoryId, Integer cookingTime, String difficulty) throws InvalidInputException {
        List<Recipe> recipes = recipeRepository.findRecipesByFilters(cuisineId, categoryId, cookingTime, DifficultyLevel.fromString(difficulty));
        return recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .toList();

    }
}
