package com.recipe.RecipeService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("recipe")
public class Controller
{
    @GetMapping("test")
    public String test()
    {
        return "recipe controller";
    }
}
