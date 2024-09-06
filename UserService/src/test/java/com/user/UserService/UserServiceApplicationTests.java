package com.user.UserService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest

class UserServiceApplicationTests {

import feign.FeignException;
@ExtendWith(MockitoExtension.class)
class UserServiceApplicationTest{

	private MockMvc mockMvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private RecipeServiceClient recipeServiceClient;

	@InjectMocks
	private AdminCuisineController adminCuisineController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(adminCuisineController).build();
	}
}





