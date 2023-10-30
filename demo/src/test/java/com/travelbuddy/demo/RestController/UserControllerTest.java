package com.travelbuddy.demo.RestController;
import com.travelbuddy.demo.Entities.User;

import com.travelbuddy.demo.Secruity.ServiceSec.JwtService;
import com.travelbuddy.demo.Services.UserSecService;
import com.travelbuddy.demo.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;



public class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private UserSecService userSecService;
    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        userService = mock(UserService.class);
        userSecService = mock(UserSecService.class);
        jwtService = mock(JwtService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, userSecService, jwtService)).build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "testpassword");
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    @Test
    public void testGetUserByUsername() throws Exception {
        // Arrange
        User user = new User("firstname", "lastname", "username","2023-01-01", "link.com", "hiking", "malle","sociallinks", User.Gender.D);
        when(userService.findByUsername("username")).thenReturn(user);

        // Act and Assert
        mockMvc.perform(get("/users/username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    public void testGetUserByUsernameNotFound() throws Exception {
        // Arrange
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        // Act and Assert
        mockMvc.perform(get("/users/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {

        User user = new User("firstname", "lastname", "username","2023-01-01", "link.com", "hiking", "malle","sociallinks", User.Gender.D);

        // Act and Assert
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("The User username were saved"));
    }

    @Test
    public void testCreateUserForbidden() throws Exception {

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"differentUsername\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Username must equal the Security username"));
    }

    @Test
    public void testDeleteUserByUsername() throws Exception {
        // Arrange
        User user = new User("firstname", "lastname", "username","2023-01-01", "link.com", "hiking", "malle","sociallinks", User.Gender.D);
        when(userService.findByUsername("username")).thenReturn(user);

        // Act and Assert
        mockMvc.perform(delete("/users/username"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserByUsernameForbidden() throws Exception {
        // Arrange
        when(userService.findByUsername("username")).thenReturn(new User("firstname", "lastname", "firstlast","2023-01-01", "link.com", "hiking", "malle","sociallinks", User.Gender.D));

        // Act and Assert
        mockMvc.perform(delete("/users/firstlast"))
                .andExpect(status().isForbidden());
    }
}
