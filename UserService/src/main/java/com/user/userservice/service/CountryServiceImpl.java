package com.user.userservice.service;
import com.user.userservice.exception.CountryAlreadyExistsException;
import com.user.userservice.exception.CountryIdNotFoundException;
import com.user.userservice.repository.CountryRepository;
import com.user.userservice.dto.CountryDTO;
import com.user.userservice.entity.Country;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{

    private final CountryRepository countryRepository;

    private final ModelMapper modelMapper;
    public List<CountryDTO> fetchCountries() {
        return countryRepository.findAll().stream()
                .map(country -> modelMapper.map(country, CountryDTO.class))
                .toList();
    }
    public CountryDTO saveCountry(  CountryDTO countryDTO) throws CountryAlreadyExistsException{
        Optional<Country> existingCountry = countryRepository.findByName(countryDTO.getName().toLowerCase());
        if (existingCountry.isPresent()) {
            throw  new CountryAlreadyExistsException(existingCountry.get().getName()+" is Already Added");

        }
        String formattedName = countryDTO.getName().substring(0, 1).toUpperCase() + countryDTO.getName().substring(1).toLowerCase();
        countryDTO.setName(formattedName);
        Country country = modelMapper.map(countryDTO, Country.class);
        country = countryRepository.save(country);
        return modelMapper.map(country, CountryDTO.class);
    }
    public CountryDTO editCountry(CountryDTO countryDTO) throws CountryAlreadyExistsException {
        Optional<Country> existingCountry = countryRepository.findByName(countryDTO.getName().toLowerCase());
        if (existingCountry.isPresent()) {
            throw  new CountryAlreadyExistsException(existingCountry.get().getName()+" is Already Added");

        }
        Optional<Country>getCountry=countryRepository.findById(countryDTO.getId());
        String formattedName = countryDTO.getName().substring(0, 1).toUpperCase() + countryDTO.getName().substring(1).toLowerCase();
        countryDTO.setId(getCountry.get().getId());
        countryDTO.setName(formattedName);
        Country country = modelMapper.map(countryDTO, Country.class);
        country = countryRepository.save(country);
        return modelMapper.map(country, CountryDTO.class);
    }
    public boolean toggleCountryStatus(Long userId) throws CountryIdNotFoundException {
        Country country = countryRepository.findById(userId)
                .orElseThrow(() -> new CountryIdNotFoundException("User not found with ID: " + userId));
        country.setEnabled(!country.isEnabled());
        countryRepository.save(country);
        return country.isEnabled();
    }
}
