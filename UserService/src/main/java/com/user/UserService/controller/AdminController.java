package com.user.UserService.controller;
import com.user.UserService.dto.CountryDTO;
import com.user.UserService.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping("/addCountries")
    public ResponseEntity<CountryDTO> addCountry(@RequestBody CountryDTO countryDTO) {
        CountryDTO savedCountry = countryService.addCountry(countryDTO);
        return ResponseEntity.ok().body(savedCountry);
    }

}
