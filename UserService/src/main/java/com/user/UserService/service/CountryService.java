package com.user.UserService.service;

import com.user.UserService.dto.CountryDTO;

import java.util.List;

public interface CountryService {
    void createCountry(CountryDTO countryDTO);
    List<CountryDTO> getAllCountries();
    CountryDTO getCountryById(Long id);
}
