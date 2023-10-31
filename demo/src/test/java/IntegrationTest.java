import com.travelbuddy.demo.DemoApplication;
import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Entities.UserSecurity;
import com.travelbuddy.demo.Secruity.Infrastructure.AuthenticationResponse;
import com.travelbuddy.demo.Secruity.Infrastructure.LoginRequest;
import com.travelbuddy.demo.Secruity.ServiceSec.JwtService;
import com.travelbuddy.demo.Services.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    private String jwtToken;
    private TestRestTemplate restTemplate = new TestRestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }


    @BeforeEach
    public void setup() {

        Authentication auth = new UsernamePasswordAuthenticationToken("username", "password", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        jwtToken = jwtService
                .generateToken("username");
    }
    @Test
    @Order(2)
    public void testLogin() {
        String loginUrl = getBaseUrl() + "/login";
        LoginRequest loginRequest = new LoginRequest("username", "password");

        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity(loginUrl, loginRequest, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        AuthenticationResponse response = responseEntity.getBody();
        assertNotNull(response);
        jwtToken = responseEntity.getBody().toString();
        assertNotNull(jwtToken);
    }

    @Test
    @Order(1)
    public void testCreateUserSec() {
        String registerUrl = getBaseUrl() + "/register";
        UserSecurity userSecurity = new UserSecurity("id", "username", "password", "email", "handy", true);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(registerUrl, userSecurity, String.class);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


        String username = responseEntity.getBody();
        assertEquals(userSecurity.getUsername(), username);
    }
    @Test
    @Order(3)
    public void testLoginWithInvalidCredentials() {
        String loginUrl = getBaseUrl() + "/login";
        LoginRequest loginRequest = new LoginRequest("invalidUser", "invalidPassword");

        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity(loginUrl, loginRequest, AuthenticationResponse.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }
    @Test
    @Order(5)
    public void testGetUserByUsername() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>("username", headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(getBaseUrl() + "/users/username",HttpMethod.GET, entity, User.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User user = responseEntity.getBody();
        assertEquals("username", user.getUsername());
    }

    @Test
    @Order(6)
    public void testGetNonExistentUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        String username = "nonexistentuser";
        HttpEntity<String> entity = new HttpEntity<>(username, headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(getBaseUrl() + "/users/"+username, HttpMethod.GET, entity, User.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    public void testCreateUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        User user = new User("firstname", "lastname", "username","2023-01-01", "link.com", "hiking", "malle","sociallinks", User.Gender.D);

        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/users/register",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        String response = responseEntity.getBody();
        assertEquals("The User username were saved", response);
    }

    @Test
    @Order(7)
    public void testCreateUserWithInvalidUsername() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        User user = new User("firstname", "lastname", "testname","2023-01-01", "link.com", "hiking", "malle","sociallinks", User.Gender.D);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/users/register",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        String response = responseEntity.getBody();
        assertEquals("Username must equal the Security username", response);
    }

    @Test
    @Order(8)
    public void testDeleteUserByUsername() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        String username = "username";
        HttpEntity<String> entity = new HttpEntity<>("username", headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/users/" + username,
                org.springframework.http.HttpMethod.DELETE,
                entity,
                String.class
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

}
