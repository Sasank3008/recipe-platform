package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.dto.UpdateRecipeDTO;
import com.recipe.recipeservice.exception.IdNotFoundException;
import com.recipe.recipeservice.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class EditRecipeController {
    private final RecipeService recipeService;
    private final ModelMapper modelMapper;
    @PutMapping(value="/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateRecipe(@ModelAttribute UpdateRecipeDTO recipeDTO) throws IdNotFoundException, IOException{
        Long id= recipeDTO.getId();
        recipeService.updateRecipe(recipeDTO,id);
      return "Recipe updated successfully";
    }
}
