package RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.AdapterClasses.TripRole;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.RestController.TripsController;
import com.travelbuddy.demo.Services.TripsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TripsControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private TripsController tripsController;
    private TripsService tripsService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        tripsService = mock(TripsService.class);

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tripsController).build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "password");
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    @WithMockUser(username = "testUser")
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
        members.add(new TripMember("testUser", TripRole.Organizer, "Active"));
        trip.setMembers(members);
        trip.setMaxPersons(12);
        trip.setTravelVehicle("Auto");
        trip.setType("Wandern");

        when(tripsService.saveTrip(any(Trips.class))).thenReturn(trip);


        mockMvc.perform(post("/trips")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testGetTrip() throws Exception {
        // Arrange
        String tripId = "your_trip_id";
        Trips trip = new Trips();
        trip.setId("1");
        trip.setStartdate("01.01.2024");
        trip.setEnddate("10.01.2024");
        trip.setDestination("Davids Oarschloch");
        trip.setDescription("Hier könnte Ihre Werbung stehen");
        trip.setCosts(200000);
        List<TripMember> members = new ArrayList<>();
        members.add(new TripMember("MaxMustermann", TripRole.Traveler, "Active"));
        members.add(new TripMember("testUser", TripRole.Organizer, "Active"));
        trip.setMembers(members);
        trip.setMaxPersons(12);
        trip.setTravelVehicle("Auto");
        trip.setType("Wandern");
        when(tripsService.getTripById(tripId)).thenReturn(Optional.of(trip));

        // Act and Assert
        mockMvc.perform(get("/trips/" + tripId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(trip.getId())); // Adjust the JSON path as needed
    }

    @Test
    public void testGetTripNotFound() throws Exception {
        // Arrange
        String tripId = "nonexistent_trip_id";
        when(tripsService.getTripById(tripId)).thenReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(get("/trips/" + tripId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testUpdateTripWithUnauthorizedUser() throws Exception {
        // Prepare test data
        Trips updatedTrip = new Trips(); // Set your updated trip details
        String tripId = "12345";
        updatedTrip.setId("1");
        updatedTrip.setStartdate("01.01.2024");
        updatedTrip.setEnddate("10.01.2024");
        updatedTrip.setDestination("Davids Oarschloch");
        updatedTrip.setDescription("Hier könnte Ihre Werbung stehen");
        updatedTrip.setCosts(200000);
        List<TripMember> members = new ArrayList<>();
        members.add(new TripMember("MaxMustermann", TripRole.Traveler, "Active"));
        members.add(new TripMember("testUser", TripRole.Organizer, "Active"));
        updatedTrip.setMembers(members);
        updatedTrip.setMaxPersons(12);
        updatedTrip.setTravelVehicle("Auto");
        updatedTrip.setType("Wandern");
        Trips existingTrip = new Trips(); // Set your existing trip details
        existingTrip.setId(tripId);

        // Mock the behavior of the service
        when(tripsService.getTripById(tripId)).thenReturn(Optional.of(existingTrip));

        // Perform the PUT request
        ResultActions result = mockMvc.perform(
                put("/trips/{tripId}", tripId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTrip)));

        // Verify the response
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testUpdateTripWithNonExistentTrip() throws Exception {
        // Prepare test data
        Trips updatedTrip = new Trips(); // Set your updated trip details
        String tripId = "12345";

        // Mock the behavior of the service
        when(tripsService.getTripById(tripId)).thenReturn(Optional.empty());

        // Perform the PUT request
        ResultActions result = mockMvc.perform(
                put("/trips/{tripId}", tripId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTrip)));

        // Verify the response
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDeleteExistingTrip() throws Exception {
        Trips trip = new Trips();
        trip.setId("1");
        trip.setStartdate("01.01.2024");
        trip.setEnddate("10.01.2024");
        trip.setDestination("Davids Oarschloch");
        trip.setDescription("Hier könnte Ihre Werbung stehen");
        trip.setCosts(200000);
        List<TripMember> members = new ArrayList<>();
        members.add(new TripMember("MaxMustermann", TripRole.Traveler, "Active"));
        members.add(new TripMember("testUser", TripRole.Organizer, "Active"));
        trip.setMembers(members);
        trip.setMaxPersons(12);
        trip.setTravelVehicle("Auto");
        trip.setType("Wandern");

        when(tripsService.deleteTrip(trip.getId())).thenReturn(true);
        when(tripsService.getTripById(trip.getId())).thenReturn(Optional.of(trip));

        mockMvc.perform(delete("/trips/{tripId}", trip.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDeleteTripUnauthorized() throws Exception {
        String tripId = "12345";

        when(tripsService.getTripById(tripId)).thenReturn(Optional.of(new Trips()));

        mockMvc.perform(delete("/trips/{tripId}", tripId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDeleteNonExistentTrip() throws Exception {
        String tripId = "12345";

        when(tripsService.getTripById(tripId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/trips/{tripId}", tripId))
                .andExpect(status().isNotFound());
    }
}
