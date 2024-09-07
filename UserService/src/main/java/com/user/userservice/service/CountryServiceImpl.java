package com.user.userservice.service;

import com.user.userservice.handler.CountryAlreadyExistsException;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.dto.CountryDTO;
import com.user.userservice.entity.Country;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{

    private final CountryRepository countryRepository;

    private final ModelMapper modelMapper;
    public List<CountryDTO> fetchCountries() {
        return countryRepository.findAll().stream()
                .map(country -> modelMapper.map(country, CountryDTO.class))
                .collect(Collectors.toList());
    }
    public CountryDTO saveCountry(  CountryDTO countryDTO) throws CountryAlreadyExistsException{
        Optional<Country> existingCountry = countryRepository.findByName(countryDTO.getName().toLowerCase());
        if (existingCountry.isPresent()) {
            throw  new CountryAlreadyExistsException(existingCountry.get().getName()+" is Already Added");
           // return modelMapper.map(existingCountry.get(), CountryDTO.class);
        }
        Country country = modelMapper.map(countryDTO, Country.class);
        country = countryRepository.save(country);
        return modelMapper.map(country, CountryDTO.class);
    }
}
