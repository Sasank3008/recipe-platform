package com.recipe.recipeservice.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.recipe.recipeservice.dto.RecipeDTO;
import com.recipe.recipeservice.service.CuisineService;
import com.recipe.recipeservice.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CuisineService cuisineService;
    @MockBean
    private RecipeService recipeService;

    @Test
    public void testSearchRecipes_ReturnsRecipesSuccessfully() throws Exception {
        String keyword = "salad";
        List<RecipeDTO> recipeDTOs = List.of(new RecipeDTO());
        when(recipeService.searchRecipes(keyword)).thenReturn(recipeDTOs);
        mockMvc.perform(get("/recipes/search")
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeList").exists())
                .andExpect(jsonPath("$.status").value("200 OK"));
        verify(recipeService).searchRecipes(keyword);
    }
}