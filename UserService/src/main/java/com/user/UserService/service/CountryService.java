package com.user.UserService.service;

import com.user.UserService.dao.CountryRepository;
import com.user.UserService.dto.CountryDto;
import com.user.UserService.entity.Country;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(country -> new CountryDto(country.getId(), country.getName()))
                .collect(Collectors.toList());
    }

    public CountryDto addCountry(CountryDto countryDTO) {
//        Country country = new Country();
//        country.setName(countryDTO.getName());
//        country = countryRepository.save(country);
//        return new CountryDto(country.getId(), country.getName());
        Optional<Country> existingCountry = countryRepository.findByName(countryDTO.getName());
        if (existingCountry.isPresent()) {
            // Return the existing country if it already exists
            return modelMapper.map(existingCountry.get(), CountryDto.class);
        }
        Country country = modelMapper.map(countryDTO, Country.class);
        country = countryRepository.save(country);
        return modelMapper.map(country, CountryDto.class);
    }


}
