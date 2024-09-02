package com.user.UserService.serviceTests;

import com.user.UserService.dto.CountryDTO;
import com.user.UserService.entity.CountryEntity;
import com.user.UserService.repository.CountryRepository;
import com.user.UserService.service.CountryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceApplicationTests {

    @InjectMocks
    private CountryServiceImpl countryService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCountry_ShouldSaveCountry() {
        // Arrange
        CountryDTO countryDTO = new CountryDTO();
        CountryEntity countryEntity = new CountryEntity();

        when(modelMapper.map(countryDTO, CountryEntity.class)).thenReturn(countryEntity);

        // Act
        countryService.createCountry(countryDTO);

        // Assert
        verify(countryRepository, times(1)).save(countryEntity);
    }

    @Test
    void getAllCountries_ShouldReturnListOfCountryDTO() {
        // Arrange
        CountryEntity countryEntity1 = new CountryEntity();
        CountryEntity countryEntity2 = new CountryEntity();
        CountryDTO countryDTO1 = new CountryDTO();
        CountryDTO countryDTO2 = new CountryDTO();

        List<CountryEntity> countryEntities = Arrays.asList(countryEntity1, countryEntity2);
        when(countryRepository.findAll()).thenReturn(countryEntities);
        when(modelMapper.map(countryEntity1, CountryDTO.class)).thenReturn(countryDTO1);
        when(modelMapper.map(countryEntity2, CountryDTO.class)).thenReturn(countryDTO2);

        // Act
        List<CountryDTO> result = countryService.getAllCountries();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(countryDTO1));
        assertTrue(result.contains(countryDTO2));
    }
}
