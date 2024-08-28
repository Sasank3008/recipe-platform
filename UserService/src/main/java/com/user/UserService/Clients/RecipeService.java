package com.user.UserService.Clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("RECIPE-SERVICE")
public interface RecipeService {

}
