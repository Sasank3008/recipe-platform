package com.user.UserService.controller;

// In user-management-service



import com.user.UserService.dto.CountryDTO;
import com.user.UserService.service.CountryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {


    @Autowired
    private CountryServiceImpl countryService;

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        List<CountryDTO> countries = countryService.getAllCountries();
        return ResponseEntity.ok().body(countries);
    }

    @PostMapping
    public ResponseEntity<CountryDTO> addCountry(@RequestBody CountryDTO countryDTO) {
        CountryDTO savedCountry = countryService.addCountry(countryDTO);
        return ResponseEntity.ok().body(savedCountry);
    }


}
