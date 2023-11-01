package Services;

import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.AdapterClasses.TripRole;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Repository.TripsRepo;
import com.travelbuddy.demo.Services.TripsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TripsServiceTest {

    @Mock
    private TripsRepo tripsRepo;

    private TripsService tripsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tripsService = new TripsService(tripsRepo);
    }

    @Test
    public void testSaveTrip() {
        Trips trip = new Trips();
        tripsService.saveTrip(trip);
        verify(tripsRepo, times(1)).save(trip);
    }

    @Test
    public void testGetTripById() {
        Trips trip = new Trips();
        Optional<Trips> optionalTrip = Optional.of(trip);

        when(tripsRepo.findById("1")).thenReturn(optionalTrip);
        Optional<Trips> result = tripsService.getTripById("1");

        assertEquals(optionalTrip, result);
    }


    @Test
    public void testUpdateTrip() {
        Trips trip = new Trips();
        when(tripsRepo.findById("1")).thenReturn(java.util.Optional.of(trip));
        tripsService.updateTrip("1", trip);
        verify(tripsRepo, times(1)).save(trip);
    }

    @Test
    public void testAddMemberToTrip() {
        Trips trip = new Trips();
        when(tripsRepo.findById("1")).thenReturn(java.util.Optional.of(trip));
        tripsService.addMemberToTrip("1", "username", TripRole.Traveler, "status");
        verify(tripsRepo, times(1)).save(trip);
    }

    @Test
    public void testRemoveMemberFromTrip() {
        Trips trip = new Trips();
        TripMember member = new TripMember("username", TripRole.Traveler, "active");
        trip.getMembers().add(member);
        when(tripsRepo.findById("1")).thenReturn(Optional.of(trip));

        tripsService.removeMemberFromTrip("1", "username");

        verify(tripsRepo, times(1)).save(trip);
    }

    @Test
    public void testChangeUserRole() {
        Trips trip = new Trips();
        TripMember member = new TripMember("username", TripRole.Organizer, "active");
        trip.getMembers().add(member);
        TripMember member2 = new TripMember("targetUser", TripRole.Traveler, "active");
        trip.getMembers().add(member2);
        when(tripsRepo.findById("1")).thenReturn(java.util.Optional.of(trip));
        tripsService.changeUserRole("1", "username", "targetUser",TripRole.Assistant.getDescription());
        verify(tripsRepo, times(1)).save(trip);
    }


    @Test
    public void testDeleteTrip() {
        Trips trip = new Trips();
        when(tripsRepo.findById("1")).thenReturn(java.util.Optional.of(trip));
        tripsService.deleteTrip("1");
        verify(tripsRepo, times(1)).deleteById("1");
    }

}
