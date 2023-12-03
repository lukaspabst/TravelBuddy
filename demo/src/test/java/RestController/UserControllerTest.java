package RestController;

import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.RestController.UserController;
import com.travelbuddy.demo.Secruity.ServiceSec.JwtService;
import com.travelbuddy.demo.Services.UserSecService;
import com.travelbuddy.demo.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

        User user = new User("firstname", "lastname", "username", "2023-01-01", "link.com", "hiking", "malle", Map.of("Twitter", "https://twitter.com/ZenayoK", "Instagram", "https://www.instagram.com/lukas_23.03"), User.Gender.D, "ebeleben", "77777");
        when(userService.findByUsername("username")).thenReturn(user);


        mockMvc.perform(get("/users/username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    public void testGetUserByUsernameNotFound() throws Exception {

        when(userService.findByUsername("nonexistent")).thenReturn(null);


        mockMvc.perform(get("/users/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserByUsername() throws Exception {

        User user = new User("firstname", "lastname", "username", "2023-01-01", "link.com", "hiking", "malle", Map.of("Twitter", "https://twitter.com/ZenayoK", "Instagram", "https://www.instagram.com/lukas_23.03"), User.Gender.D, "ebeleben", "77777");
        when(userService.findByUsername("username")).thenReturn(user);


        mockMvc.perform(delete("/users/username"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserByUsernameForbidden() throws Exception {

        when(userService.findByUsername("username")).thenReturn(new User("firstname", "lastname", "firstlast", "2023-01-01", "link.com", "hiking", "malle", Map.of("Twitter", "https://twitter.com/ZenayoK", "Instagram", "https://www.instagram.com/lukas_23.03"), User.Gender.D, "ebeleben", "77777"));


        mockMvc.perform(delete("/users/firstlast"))
                .andExpect(status().isForbidden());
    }
}
