package com.user.UserService;
import com.user.UserService.dao.CountryRepository;
import com.user.UserService.dto.CountryDTO;
import com.user.UserService.entity.Country;
import com.user.UserService.service.CountryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CountryServiceImpl countryService;
    @Test
    public void testAddCountry() {
        CountryDTO countryDTO = new CountryDTO(null, "USA");
        Country country = new Country(null, "USA");
        Country savedCountry = new Country(1L, "USA");
        CountryDTO savedCountryDTO = new CountryDTO(1L, "USA");
        when(modelMapper.map(countryDTO, Country.class)).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(savedCountry);
        when(modelMapper.map(savedCountry, CountryDTO.class)).thenReturn(savedCountryDTO);
        CountryDTO result = countryService.addCountry(countryDTO);
        assertEquals(savedCountryDTO, result);
        verify(countryRepository, times(1)).save(country);
        verify(modelMapper, times(1)).map(countryDTO, Country.class);
        verify(modelMapper, times(1)).map(savedCountry, CountryDTO.class);
    }
   @Test
   void testGetAllCountries() {

       Country country1 = new Country(1L, "USA");
       Country country2 = new Country(2L, "Canada");
       List<Country> countries = Arrays.asList(country1, country2);
       CountryDTO dto1 = new CountryDTO(country1.getId(), country1.getName());
       CountryDTO dto2 = new CountryDTO(country2.getId(), country2.getName());
       when(countryRepository.findAll()).thenReturn(countries);
       when(modelMapper.map(country1, CountryDTO.class)).thenReturn(dto1);
       when(modelMapper.map(country2, CountryDTO.class)).thenReturn(dto2);
       List<CountryDTO> result = countryService.getAllCountries();
       assertEquals(2, result.size());
       assertEquals("USA", result.get(0).getName());
       assertEquals("Canada", result.get(1).getName());
       verify(modelMapper).map(country1, CountryDTO.class);
       verify(modelMapper).map(country2, CountryDTO.class);
       verifyNoMoreInteractions(modelMapper);
   }
    @Test
    void testAddCountry_ExistingCountry() {
        // Arrange
        String countryName = "Mexico";
        Country existingCountry = new Country(3L, countryName);
        CountryDTO existingCountryDTO = new CountryDTO(3L, countryName);
        when(countryRepository.findByName(countryName)).thenReturn(Optional.of(existingCountry));
        when(modelMapper.map(existingCountry, CountryDTO.class)).thenReturn(existingCountryDTO);
        CountryDTO newCountryDTO = new CountryDTO(null, countryName);
        CountryDTO result = countryService.addCountry(newCountryDTO);
        assertEquals(existingCountryDTO.getId(), result.getId());
        assertEquals(existingCountryDTO.getName(), result.getName());
        verify(countryRepository, never()).save(any(Country.class)); // Ensure no save operation
    }
}

