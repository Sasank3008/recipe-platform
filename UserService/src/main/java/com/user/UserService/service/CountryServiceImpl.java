package com.user.UserService.service;

import com.user.UserService.dto.CountryDTO;
import com.user.UserService.entity.Country;
import com.user.UserService.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createCountry(CountryDTO countryDTO){
        Country country = modelMapper.map(countryDTO, Country.class);
        countryRepository.save(country);
    }

    @Override
    public List<CountryDTO> getAllCountries(){
        List<Country> countries = countryRepository.findAll();
        return  countries.stream()
                .map(country -> modelMapper.map(country,CountryDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public CountryDTO getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        return modelMapper.map(country, CountryDTO.class);
    }

}
