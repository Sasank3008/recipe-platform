package com.recipe.recipeservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recipe")
public class RecipeController
{
    @GetMapping("health")
    public ResponseEntity<String> test()
    {
        return new ResponseEntity<>("Recipe controller is working", HttpStatus.OK);
    }
}
