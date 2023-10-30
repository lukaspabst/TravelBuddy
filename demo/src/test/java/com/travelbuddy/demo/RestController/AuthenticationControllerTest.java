package com.travelbuddy.demo.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbuddy.demo.Entities.UserSecurity;
import com.travelbuddy.demo.Secruity.Infrastructure.AuthenticationResponse;
import com.travelbuddy.demo.Secruity.Infrastructure.LoginRequest;
import com.travelbuddy.demo.Secruity.ServiceSec.AuthenticationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        AuthenticationResponse response = new AuthenticationResponse("token");

        when(authenticationService.login(loginRequest)).thenReturn(response);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(response)));

        verify(authenticationService, times(1)).login(loginRequest);
    }

    @Test
    void testCreateUser() throws Exception {
        UserSecurity userSecurity = new UserSecurity("id","username", "password", "email", "handy",true);

        AuthenticationResponse mockResponse = AuthenticationResponse.builder()
                .jwtToken("your_mocked_jwt_token")
                .build();

        when(authenticationService.register(any(UserSecurity.class))).thenReturn(mockResponse);


        mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(userSecurity)))
                    .andExpect(status().isOk());


        verify(authenticationService, times(1)).register(userSecurity);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
