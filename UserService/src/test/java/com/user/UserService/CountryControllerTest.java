package com.user.UserService;
import com.user.UserService.controller.CountryController;
import com.user.UserService.dto.CountryDTO;
import com.user.UserService.service.CountryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class CountryControllerTest {

    @Mock
    private CountryServiceImpl countryService;

    @InjectMocks
    private CountryController countryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(countryController).build();
    }
    @Test
    void testGetAllCountries() throws Exception {
        // Arrange
        CountryDTO country1 = new CountryDTO(1L, "USA");
        CountryDTO country2 = new CountryDTO(2L, "Canada");
        List<CountryDTO> countries = Arrays.asList(country1, country2);

        when(countryService.getAllCountries()).thenReturn(countries);

        // Act & Assert
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("USA"))
                .andExpect(jsonPath("$[1].name").value("Canada"));

        verify(countryService).getAllCountries();
    }
    @Test
    void testAddCountry() throws Exception {
        // Arrange
        CountryDTO newCountry = new CountryDTO(null, "Mexico");
        CountryDTO savedCountry = new CountryDTO(3L, "Mexico");

        when(countryService.addCountry(any(CountryDTO.class))).thenReturn(savedCountry);

        // Act & Assert
        mockMvc.perform(post("/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mexico\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mexico"))
                .andExpect(jsonPath("$.id").value(3));

        verify(countryService).addCountry(any(CountryDTO.class));
    }
}
