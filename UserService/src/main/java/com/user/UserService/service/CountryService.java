package com.user.UserService.service;

import com.user.UserService.dto.CountryDTO;

import java.util.List;

public interface CountryService {
    public CountryDTO addCountry(CountryDTO countryDTO);
    public List<CountryDTO> getAllCountries();
}
