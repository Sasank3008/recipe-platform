package com.admin.AdminService.service;

import com.user.UserService.dto.CountryDto;

import java.util.List;

public interface AdminCountryService {

    public List<CountryDto> getCountries();



    public CountryDto addCountry(CountryDto countryDTO);


}
