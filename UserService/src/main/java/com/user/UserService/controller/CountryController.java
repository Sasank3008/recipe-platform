package com.user.UserService.controller;

import com.user.UserService.dto.CountryDTO;
import com.user.UserService.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping
    public ResponseEntity<?> saveCountry(@Valid @RequestBody CountryDTO countryDTO){
        countryService.createCountry(countryDTO);
        return new ResponseEntity<>("Country added successfully",HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?>getAllCountries(){
        return new ResponseEntity<>(countryService.getAllCountries(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCountryById(@PathVariable Long id) {
        CountryDTO countryDTO = countryService.getCountryById(id);
        return new ResponseEntity<>(countryDTO, HttpStatus.OK);
    }

}
