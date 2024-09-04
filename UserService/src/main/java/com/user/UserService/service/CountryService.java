package com.user.UserService.service;

import com.user.UserService.dto.CountryDTO;

import java.util.List;

public interface CountryService {
    public CountryDTO saveCountry(CountryDTO countryDTO);
    public List<CountryDTO> fetchCountries();
}
