package com.recipe.recipeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@EntityScan("com.recipe.recipeservice.entity")
//@EnableJpaRepositories("com.recipe.recipeservice.repository")
public class RecipeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeServiceApplication.class, args);
	}

}
