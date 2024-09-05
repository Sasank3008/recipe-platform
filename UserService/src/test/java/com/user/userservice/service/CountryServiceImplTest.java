package com.user.userservice.service;

import com.user.userservice.dto.CountryDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.repository.CountryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

    @Test
    void testFetchCountries() {
        // Arrange
        Country country = new Country(1L, "USA");
        List<Country> countryList = Arrays.asList(country);
        when(countryRepository.findAll()).thenReturn(countryList);
        when(modelMapper.map(any(Country.class), eq(CountryDTO.class))).thenReturn(new CountryDTO(1L, "USA"));

        // Act
        List<CountryDTO> result = countryService.fetchCountries();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("USA", result.get(0).getName());
        verify(countryRepository).findAll();
        verify(modelMapper, times(1)).map(any(Country.class), eq(CountryDTO.class));
    }

    @Test
    void testSaveCountry_NewCountry() {
        // Arrange
        CountryDTO newCountryDTO = new CountryDTO(null, "Canada");
        Country savedCountry = new Country(2L, "Canada");
        when(countryRepository.findByName("Canada")).thenReturn(Optional.empty());
        when(modelMapper.map(any(CountryDTO.class), eq(Country.class))).thenReturn(savedCountry);
        when(countryRepository.save(any(Country.class))).thenReturn(savedCountry);
        when(modelMapper.map(any(Country.class), eq(CountryDTO.class))).thenReturn(new CountryDTO(2L, "Canada"));

        // Act
        CountryDTO result = countryService.saveCountry(newCountryDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Canada", result.getName());
        verify(countryRepository).findByName("Canada");
        verify(countryRepository).save(any(Country.class));
        verify(modelMapper, times(2)).map(any(), any());
    }

    @Test
    void testSaveCountry_ExistingCountry() {
        // Arrange
        CountryDTO existingCountryDTO = new CountryDTO(null, "USA");
        Country existingCountry = new Country(1L, "USA");
        when(countryRepository.findByName("USA")).thenReturn(Optional.of(existingCountry));
        when(modelMapper.map(existingCountry, CountryDTO.class)).thenReturn(new CountryDTO(1L, "USA"));

        // Act
        CountryDTO result = countryService.saveCountry(existingCountryDTO);

        // Assert
        assertNotNull(result);
        assertEquals("USA", result.getName());
        verify(countryRepository).findByName("USA");
        verify(countryRepository, never()).save(any(Country.class));
        verify(modelMapper).map(existingCountry, CountryDTO.class);
    }
}