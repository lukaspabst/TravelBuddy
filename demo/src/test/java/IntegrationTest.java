import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.AdapterClasses.TripRole;
import com.travelbuddy.demo.DemoApplication;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Entities.UserSecurity;
import com.travelbuddy.demo.Secruity.Infrastructure.AuthenticationResponse;
import com.travelbuddy.demo.Secruity.Infrastructure.LoginRequest;
import com.travelbuddy.demo.Secruity.ServiceSec.JwtService;
import com.travelbuddy.demo.Services.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private String jwtToken;
    private final TestRestTemplate restTemplate = new TestRestTemplate();

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
        ResponseEntity<User> responseEntity = restTemplate.exchange(getBaseUrl() + "/users/username", HttpMethod.GET, entity, User.class);

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
        ResponseEntity<User> responseEntity = restTemplate.exchange(getBaseUrl() + "/users/" + username, HttpMethod.GET, entity, User.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    public void testCreateUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        User user = new User("firstname", "lastname", "username", "2023-01-01", null, "hiking", "malle", Map.of("Twitter", "https://twitter.com/ZenayoK", "Instagram", "https://www.instagram.com/lukas_23.03"), User.Gender.D, "ebeleben", "77777");

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
        User user = new User("firstname", "lastname", "testname", "2023-01-01", null, "hiking", "malle", Map.of("Twitter", "https://twitter.com/ZenayoK", "Instagram", "https://www.instagram.com/lukas_23.03"), User.Gender.D, "ebeleben", "77777");
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
    @Order(20)
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

    @Test
    @Order(8)
    public void testCreateTrip() throws Exception {
        Trips trip = new Trips();
        trip.setId("1");
        trip.setStartdate("01.01.2024");
        trip.setEnddate("10.01.2024");
        trip.setDestination("Davids Oarschloch");
        trip.setDescription("Hier könnte Ihre Werbung stehen");
        trip.setCosts(200000);
        List<TripMember> members = new ArrayList<>();
        members.add(new TripMember("MaxMustermann", TripRole.Traveler, "Active"));
        members.add(new TripMember("testUser", TripRole.Traveler, "Active"));
        trip.setMembers(members);
        trip.setMaxPersons(12);
        trip.setTravelVehicle("Auto");
        trip.setType("Wandern");


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Trips> entity = new HttpEntity<>(trip, headers);


        ResponseEntity<Trips> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/trips",
                HttpMethod.POST,
                entity,
                Trips.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Trips createdTrip = responseEntity.getBody();
        assertNotNull(createdTrip);
    }

    @Test
    @Order(9)
    public void testGetTrip() {
        String tripId = "1";
        String getTripUrl = getBaseUrl() + "/trips/" + tripId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Trips> responseEntity = restTemplate.exchange(
                getTripUrl,
                HttpMethod.GET,
                entity,
                Trips.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Trips trip = responseEntity.getBody();
        assertNotNull(trip);
    }

    @Test
    @Order(10)
    public void testUpdateTrip() {
        String tripId = "1";
        String updateTripUrl = getBaseUrl() + "/trips/" + tripId;

        Trips updatedTrip = new Trips();
        updatedTrip.setId("1");
        updatedTrip.setStartdate("01.01.2034");
        updatedTrip.setEnddate("10.01.2034");
        updatedTrip.setDestination("Davids Oarschloch");
        updatedTrip.setDescription("Hier könnte Ihre Werbung stehen");
        updatedTrip.setCosts(200000);
        List<TripMember> members = new ArrayList<>();
        members.add(new TripMember("MaxMustermann", TripRole.Traveler, "Active"));
        members.add(new TripMember("testUser", TripRole.Traveler, "Active"));
        updatedTrip.setMembers(members);
        updatedTrip.setMaxPersons(12);
        updatedTrip.setTravelVehicle("Auto");
        updatedTrip.setType("Wandern");


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Trips> entity = new HttpEntity<>(updatedTrip, headers);

        ResponseEntity<Trips> responseEntity = restTemplate.exchange(
                updateTripUrl,
                HttpMethod.PUT,
                entity,
                Trips.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Trips updated = responseEntity.getBody();
        assertNotNull(updated);
    }

    @Test
    @Order(11)
    public void testCreateTripBadRequest() {
        String createTripUrl = getBaseUrl() + "/trips";

        Trips invalidTrip = new Trips();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Trips> entity = new HttpEntity<>(invalidTrip, headers);

        ResponseEntity<Trips> responseEntity = restTemplate.exchange(
                createTripUrl,
                HttpMethod.POST,
                entity,
                Trips.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    @Order(12)
    public void testGetNonExistentTrip() {
        String nonExistentTripId = "non_existent_trip_id";
        String getTripUrl = getBaseUrl() + "/trips/" + nonExistentTripId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Trips> responseEntity = restTemplate.exchange(
                getTripUrl,
                HttpMethod.GET,
                entity,
                Trips.class
        );

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @Order(13)
    public void testUpdateTripUnauthorized() {
        String tripId = "1";
        String updateTripUrl = getBaseUrl() + "/trips/" + tripId;

        Trips updatedTrip = new Trips();
        String unauthorizedJwtToken = "your_unauthorized_jwt_token_here";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(unauthorizedJwtToken);
        HttpEntity<Trips> entity = new HttpEntity<>(updatedTrip, headers);

        ResponseEntity<Trips> responseEntity = restTemplate.exchange(
                updateTripUrl,
                HttpMethod.PUT,
                entity,
                Trips.class
        );

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @Order(14)
    public void testAddMemberToTripUnauthorized() throws Exception {
        String tripId = "1";
        String addMemberUrl = getBaseUrl() + "/trips/" + tripId + "/addMember";
        Authentication auth = new UsernamePasswordAuthenticationToken("your_unauthorized_jwt_token_here", "password", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        TripMember newMember = new TripMember("new_member", TripRole.Traveler, "Active");
        String unauthorizedJwtToken = jwtService.generateToken("your_unauthorized_jwt_token_here");

        mockMvc.perform(MockMvcRequestBuilders.patch(addMemberUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMember))
                        .header("Authorization", "Bearer " + unauthorizedJwtToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Order(15)
    public void testRemoveMemberFromTripUnauthorized() throws Exception {
        String tripId = "1";
        String username = "username_to_remove";
        String removeMemberUrl = getBaseUrl() + "/trips/" + tripId + "/removeMember/" + username;

        String unauthorizedJwtToken = jwtService.generateToken("your_unauthorized_jwt_token_here");

        mockMvc.perform(MockMvcRequestBuilders.patch(removeMemberUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(username))
                        .header("Authorization", "Bearer " + unauthorizedJwtToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden()
                );
    }

    @Test
    @Order(16)
    public void testAddMemberToTrip() throws Exception {
        String tripId = "1";
        String addMemberUrl = getBaseUrl() + "/trips/" + tripId + "/addMember";

        TripMember newMember = new TripMember("new_member", TripRole.Traveler, "Active");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<TripMember> entity = new HttpEntity<>(newMember, headers);

        mockMvc.perform(MockMvcRequestBuilders.patch(addMemberUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMember))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(17)
    public void testRemoveMemberFromTrip() throws Exception {
        String tripId = "1";
        String username = "new_member";
        String removeMemberUrl = getBaseUrl() + "/trips/" + tripId + "/removeMember/" + username;

        mockMvc.perform(MockMvcRequestBuilders.patch(removeMemberUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(username))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @Order(19)
    public void testDeleteTrip_Success() {
        String tripId = "1";
        String deleteTripUrl = getBaseUrl() + "/trips/" + tripId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                deleteTripUrl,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    @Order(18)
    public void testDeleteTrip_Error() {
        String tripId = "1";
        String deleteTripUrl = getBaseUrl() + "/trips/" + tripId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("unauthorized_jwt_token");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                deleteTripUrl,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }
}


