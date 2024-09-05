package com.user.userservice.service;

import com.user.userservice.dto.CountryDTO;

import java.util.List;

public interface CountryService {
    public CountryDTO saveCountry(CountryDTO countryDTO);
    public List<CountryDTO> fetchCountries();
}
