package com.user.UserService.controller;

// In user-management-service



import com.user.UserService.dao.CountryRepository;
import com.user.UserService.dto.CountryDto;
import com.user.UserService.entity.Country;
import com.user.UserService.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {


    @Autowired
    private CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        List<CountryDto> countries = countryService.getAllCountries();
        return ResponseEntity.ok().body(countries);
    }

    @PostMapping
    public ResponseEntity<CountryDto> addCountry(@RequestBody CountryDto countryDTO) {
        CountryDto savedCountry = countryService.addCountry(countryDTO);
        return ResponseEntity.ok().body(savedCountry);
    }

}
