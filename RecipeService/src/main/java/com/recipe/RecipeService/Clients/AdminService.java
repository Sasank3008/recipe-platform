package com.recipe.RecipeService.Clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("ADMIN-SERVICE")
public interface AdminService {
}
