package com.admin.AdminService.controller;

import com.admin.AdminService.service.AdminCountryImpl;
import com.admin.AdminService.service.AdminCountryService;
import com.user.UserService.dto.CountryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @GetMapping("health")
    public ResponseEntity<String> test()
    {
        return new ResponseEntity<>("Admin controller is working", HttpStatus.OK);

    }


    @Autowired
    private AdminCountryService adminCountryImpl;

    @GetMapping("/countries")
    public ResponseEntity<List<CountryDto>> getCountries() {
        List<CountryDto> countries = adminCountryImpl.getCountries();
        return ResponseEntity.ok().body(countries);
    }

    @PostMapping("/countries")
    public ResponseEntity<CountryDto> addCountry(@RequestBody CountryDto countryDTO) {
        CountryDto savedCountry = adminCountryImpl.addCountry(countryDTO);
        return ResponseEntity.ok().body(savedCountry);
    }

}
