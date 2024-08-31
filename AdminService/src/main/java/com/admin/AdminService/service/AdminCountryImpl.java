package com.admin.AdminService.service;

import com.admin.AdminService.Clients.UserService;
import com.user.UserService.dto.CountryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCountryImpl implements AdminCountryService {

    @Autowired
    private UserService userManagementClient;

    public List<CountryDto> getCountries() {
        return userManagementClient.getCountries();
    }

    public CountryDto addCountry(CountryDto countryDTO) {
        return userManagementClient.addCountry(countryDTO);
    }
}
