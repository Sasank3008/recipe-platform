package com.user.UserService.service;

import com.user.UserService.dto.CountryDTO;
import com.user.UserService.entity.CountryEntity;
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
        CountryEntity countryEntity = modelMapper.map(countryDTO, CountryEntity.class);
        countryRepository.save(countryEntity);
    }

    @Override
    public List<CountryDTO> getAllCountries(){
        List<CountryEntity> countries = countryRepository.findAll();
        return  countries.stream()
                .map(country -> modelMapper.map(country,CountryDTO.class))
                .collect(Collectors.toList());
    }

}
