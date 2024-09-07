package com.user.userservice.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.user.userservice.dto.ApiResponse;
import com.user.userservice.dto.CountryDTO;
import com.user.userservice.handler.CountryAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.user.userservice.handler.GlobalExceptionHandler;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;
    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void whenCountryAlreadyExistsException_thenRespondWithHttpStatusImUsed() throws Exception {
        CountryAlreadyExistsException exception = new CountryAlreadyExistsException("Country Already Found");
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleCountryAlreadyExistsException(exception);
        assertEquals(HttpStatus.IM_USED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Country Already Found", response.getBody().getResponse());
    }
    @Test
    void whenMethodArgumentNotValidException_thenRespondWithBadRequest() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new CountryDTO(), "countryDTO");
        bindingResult.addError(new FieldError("countryDTO", "name", "must not be blank"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<ApiResponse> response = handler.handleMethodArgumentNotValidException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getResponse().contains("must not be blank"));
    }
}
