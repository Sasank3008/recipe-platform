package com.admin.AdminService.Clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("RECIPE-SERVICE")
public interface RecipeService {
}
