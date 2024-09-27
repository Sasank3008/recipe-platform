package com.user.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CuisineListDTO {
    private String timestamp;
    private String status;
    private List<Cuisine> cuisineList;
}
