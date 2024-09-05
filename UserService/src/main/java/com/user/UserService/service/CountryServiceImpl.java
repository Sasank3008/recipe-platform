package com.user.UserService.service;

import com.user.UserService.dao.CountryRepository;
import com.user.UserService.dto.CountryDTO;
import com.user.UserService.entity.Country;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service

public class CountryServiceImpl implements CountryService{
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ModelMapper modelMapper;
    public List<CountryDTO> fetchCountries() {
        return countryRepository.findAll().stream()
                .map(country -> modelMapper.map(country, CountryDTO.class))
                .collect(Collectors.toList());
    }
    public CountryDTO saveCountry(CountryDTO countryDTO) {

        Optional<Country> existingCountry = countryRepository.findByName(countryDTO.getName());
        if (existingCountry.isPresent()) {
            return modelMapper.map(existingCountry.get(), CountryDTO.class);
        }
        Country country = modelMapper.map(countryDTO, Country.class);
        country = countryRepository.save(country);
        return modelMapper.map(country, CountryDTO.class);
    }
}
