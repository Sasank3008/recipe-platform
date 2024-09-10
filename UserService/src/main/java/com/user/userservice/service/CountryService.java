package com.user.userservice.service;

import com.user.userservice.dto.CountryDTO;
import com.user.userservice.exception.CountryAlreadyExistsException;

import java.util.List;

public interface CountryService {
    public CountryDTO saveCountry(CountryDTO countryDTO) throws CountryAlreadyExistsException;
    public List<CountryDTO> fetchCountries();
}
