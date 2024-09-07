package com.user.userservice.controller;
import com.user.userservice.dto.CountryDTO;
import com.user.userservice.handler.CountryAlreadyExistsException;
import com.user.userservice.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController
{

    private final CountryService countryService;
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDTO>> fetchAllCountries() {
        List<CountryDTO> countries = countryService.fetchCountries();
        return ResponseEntity.ok().body(countries);
    }
    @PostMapping("/countries")
    public ResponseEntity<CountryDTO> saveCountry(@RequestBody @Valid CountryDTO countryDTO) throws MethodArgumentNotValidException, CountryAlreadyExistsException {
        CountryDTO savedCountry = countryService.saveCountry(countryDTO);
        return ResponseEntity.ok().body(savedCountry);
    }
}
