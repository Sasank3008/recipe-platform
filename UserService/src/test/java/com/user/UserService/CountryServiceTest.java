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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

//    @Test
//    public void testGetAllCountries() {
//        Country country = new Country(1L, "USA");
//        CountryDto countryDTO = new CountryDto(1L, "USA");
//
//        when(countryRepository.findAll()).thenReturn(Arrays.asList(country));
//        when(modelMapper.map(country, CountryDto.class)).thenReturn(countryDTO);
//
//        List<CountryDto> result = countryService.getAllCountries();
//
//        assertEquals(1, result.size());
//        assertEquals(countryDTO.getName(), result.get(0).getName()); // This should now pass
//
////        verify(countryRepository, times(1)).findAll();
////        verify(modelMapper, times(1)).map(country, CountryDto.class);
//
//
//
//    }


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
}
