package com.user.UserService.controllerTests;

import com.user.UserService.controller.CountryController;
import com.user.UserService.dto.CountryDTO;
import com.user.UserService.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryControllerTests {

    @InjectMocks
    private CountryController countryController;

    @Mock
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCountry_ShouldReturnCreatedStatus() {
        CountryDTO countryDTO = new CountryDTO();
        ResponseEntity<?> response = countryController.saveCountry(countryDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Country added successfully", response.getBody());
        verify(countryService, times(1)).createCountry(countryDTO);
    }

    @Test
    void getAllCountries_ShouldReturnListOfCountries() {
        List<CountryDTO> countryDTOList = Collections.singletonList(new CountryDTO());
        when(countryService.getAllCountries()).thenReturn(countryDTOList);
        ResponseEntity<?> response = countryController.getAllCountries();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countryDTOList, response.getBody());
    }
}
