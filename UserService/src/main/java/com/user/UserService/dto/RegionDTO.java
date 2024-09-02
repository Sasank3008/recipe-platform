package com.user.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RegionDTO {
    private Long id;
    private String name;
   private CountryDTO country;


    public RegionDTO(long l, String regionName) {
        this.id=l;
        this.name=regionName;
    }

}

