package com.recipe.recipeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDisplayDTO {
    private Long id;               // User ID
    private String email;           // User Email
    private String profileImageUrl;
}
