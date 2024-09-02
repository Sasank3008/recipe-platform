package com.user.UserService.controller;

import com.user.UserService.dto.CountryDTO;
import com.user.UserService.service.CountryService;
import com.user.UserService.service.CountryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    private CountryService countryService;

    @GetMapping("/getCountries")
    public ResponseEntity<List<CountryDTO>> getCountries() {
        List<CountryDTO> countries = countryService.getAllCountries();
        return ResponseEntity.ok().body(countries);
    }
    @PostMapping("/countries")
    public ResponseEntity<CountryDTO> addCountry(@RequestBody CountryDTO countryDTO) {
        CountryDTO savedCountry = countryService.addCountry(countryDTO);
        return ResponseEntity.ok().body(savedCountry);
    }

}
