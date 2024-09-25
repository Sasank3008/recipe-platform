package com.user.userservice.service;

import com.user.userservice.dto.CountryDTO;
import com.user.userservice.exception.CountryAlreadyExistsException;
import com.user.userservice.exception.CountryIdNotFoundException;

import java.util.List;

public interface CountryService {
    public CountryDTO saveCountry(CountryDTO countryDTO) throws CountryAlreadyExistsException;
    public List<CountryDTO> fetchCountries();
    CountryDTO editCountry(CountryDTO countryDTO) throws CountryAlreadyExistsException;
    boolean toggleCountryStatus(Long userId) throws CountryIdNotFoundException;
}
