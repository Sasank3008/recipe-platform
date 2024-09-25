package com.recipe.recipeservice.controller;

import com.recipe.recipeservice.dto.UpdateRecipeDTO;
import com.recipe.recipeservice.exception.IdNotFoundException;
import com.recipe.recipeservice.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EditRecipeControllerTest {

    @InjectMocks
    private EditRecipeController editRecipeController;

    @Mock
    private RecipeService recipeService;

    private UpdateRecipeDTO recipeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeDTO = new UpdateRecipeDTO();
        recipeDTO.setId(1L); // Set a sample ID for testing
        // Set other fields as needed
    }

    @Test
    void updateRecipe_Success() throws IdNotFoundException, IOException {
        // Arrange
        doNothing().when(recipeService).updateRecipe(recipeDTO, recipeDTO.getId());

        // Act
        String response = editRecipeController.updateRecipe(recipeDTO);

        // Assert
        assertEquals("Recipe updated successfully", response);
        verify(recipeService, times(1)).updateRecipe(recipeDTO, recipeDTO.getId());
    }

    @Test
    void updateRecipe_IdNotFoundException() throws IdNotFoundException, IOException {
        // Arrange
        doThrow(new IdNotFoundException("ID not found")).when(recipeService).updateRecipe(recipeDTO, recipeDTO.getId());

        // Act & Assert
        try {
            editRecipeController.updateRecipe(recipeDTO);
        } catch (IdNotFoundException e) {
            assertEquals("ID not found", e.getMessage());
            verify(recipeService, times(1)).updateRecipe(recipeDTO, recipeDTO.getId());
        }
    }

    @Test
    void updateRecipe_IOException() throws IdNotFoundException, IOException {
        // Arrange
        doThrow(new IOException("IO error occurred")).when(recipeService).updateRecipe(recipeDTO, recipeDTO.getId());

        // Act & Assert
        try {
            editRecipeController.updateRecipe(recipeDTO);
        } catch (IOException e) {
            assertEquals("IO error occurred", e.getMessage());
            verify(recipeService, times(1)).updateRecipe(recipeDTO, recipeDTO.getId());
        }
    }
}
