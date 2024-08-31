package com.admin.AdminService.Clients;

import com.user.UserService.dto.CountryDto;
import com.user.UserService.entity.Country;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("USER-SERVICE")
public interface UserService {
    @GetMapping("/countries")
    List<CountryDto> getCountries();

    @PostMapping("/countries")
    CountryDto addCountry(@RequestBody CountryDto countryDTO);
}
