package com.user.UserService.controller;
import com.user.UserService.dto.CountryDTO;
import com.user.UserService.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
@RestController

@RequestMapping("/admins")
public class AdminController
{   @Autowired
    private CountryService countryService;
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDTO>> fetchAllCountries() {
        List<CountryDTO> countries = countryService.fetchCountries();
        return ResponseEntity.ok().body(countries);
    }
    @PostMapping("/countries")
    public ResponseEntity<CountryDTO> saveCountry(@RequestBody CountryDTO countryDTO) {
        CountryDTO savedCountry = countryService.saveCountry(countryDTO);
        return ResponseEntity.ok().body(savedCountry);
    }
}
