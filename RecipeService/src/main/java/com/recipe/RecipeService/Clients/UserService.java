package com.recipe.RecipeService.Clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("USER-SERVICE")
public interface UserService {
}
