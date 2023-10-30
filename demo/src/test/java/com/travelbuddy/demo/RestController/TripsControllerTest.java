package com.travelbuddy.demo.RestController;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

public class TripsControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private TripsController tripsController;
    private TripsService tripsService;

    @BeforeEach
    public void setup() {
        tripsService = mock(TripsService.class);

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tripsController).build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "testpassword");
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    @WithMockUser(username = "testUser")
    public void testCreateTrip() throws Exception {
        // Create a sample trip for the request payload
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
        // Mock the tripsService
        when(tripsService.saveTrip(any(Trips.class))).thenReturn(trip);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("testUser", "password"));
        SecurityContextHolder.setContext(securityContext);

        // Perform the POST request
        mockMvc.perform(post("/trips")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }


    @Test
    public void testGetTrip() throws Exception {
        // Arrange
        String tripId = "your_trip_id";
        Trips trip = new Trips(/* provide necessary data */);
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
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
