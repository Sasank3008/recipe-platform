package com.user.userservice.service;

import com.user.userservice.dto.CountryDTO;
import com.user.userservice.entity.Country;
import com.user.userservice.handler.CountryAlreadyExistsException;
import com.user.userservice.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        List<Country> countryList = List.of(country);
        when(countryRepository.findAll()).thenReturn(countryList);
        when(modelMapper.map(any(Country.class), eq(CountryDTO.class))).thenReturn(new CountryDTO(1L, "Usa"));

        // Act
        List<CountryDTO> result = countryService.fetchCountries();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Usa", result.get(0).getName());
        verify(countryRepository).findAll();
        verify(modelMapper, times(1)).map(any(Country.class), eq(CountryDTO.class));
    }
    @Test
    void testSaveNewCountry_Success() throws CountryAlreadyExistsException {
        // Arrange
        String countryName = "Canada";
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName(countryName);
        Country newCountry = new Country();
        newCountry.setName(countryName);
        Country savedCountry = new Country();
        savedCountry.setName(countryName);
        when(countryRepository.findByName(countryName.toLowerCase())).thenReturn(Optional.empty());
        when(modelMapper.map(countryDTO, Country.class)).thenReturn(newCountry);
        when(countryRepository.save(newCountry)).thenReturn(savedCountry);
        when(modelMapper.map(savedCountry, CountryDTO.class)).thenReturn(countryDTO);
        CountryDTO result = countryService.saveCountry(countryDTO);
        assertNotNull(result);
        assertEquals(countryName, result.getName());
        verify(countryRepository).findByName(countryName.toLowerCase());
        verify(countryRepository).save(newCountry);
        verify(modelMapper).map(countryDTO, Country.class);
        verify(modelMapper).map(savedCountry, CountryDTO.class);
    }
    @Test
    void testSaveCountry_ThrowsCountryAlreadyExistsException() {
        // Arrange
        String countryName = "Germany";
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName(countryName);
        Country existingCountry = new Country();
        existingCountry.setName(countryName);
        when(countryRepository.findByName(countryName.toLowerCase())).thenReturn(Optional.of(existingCountry));
        // Act & Assert
        CountryAlreadyExistsException exception = assertThrows(
                CountryAlreadyExistsException.class,
                () -> countryService.saveCountry(countryDTO),
                "Expected saveCountry to throw, but it didn't"
        );
        assertTrue(exception.getMessage().contains("is Already Added"));
        verify(countryRepository).findByName(countryName.toLowerCase());
        verify(modelMapper, never()).map(any(), eq(Country.class));
        verify(countryRepository, never()).save(any(Country.class));
    }
}