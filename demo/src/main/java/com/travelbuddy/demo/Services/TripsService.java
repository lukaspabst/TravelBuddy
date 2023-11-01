package com.travelbuddy.demo.Services;

import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.AdapterClasses.TripRole;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Repository.TripsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TripsService {

    @Autowired
    private TripsRepo tripsRepo;
    public TripsService(TripsRepo tripsRepo) {
        this.tripsRepo = tripsRepo;
    }


    public Trips saveTrip(Trips trip) {
        try {
            return tripsRepo.save(trip);
        } catch (Exception e) {
            log.error("Error saving trip: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Optional<Trips> getTripById(String id) {
        try {
            return tripsRepo.findById(id);
        } catch (Exception e) {
            log.error("Error getting trip by ID: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Trips updateTrip(String id, Trips updatedTrip) {
        try {
            Optional<Trips> existingTripOptional = tripsRepo.findById(id);

            if (existingTripOptional.isPresent()) {
                Trips existingTrip = existingTripOptional.get();
                updatedTrip.setId(existingTrip.getId());

                return saveTrip(updatedTrip);

            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error updating trip: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Trips addMemberToTrip(String tripId, String username, TripRole role, String status) {
        try {
            Optional<Trips> tripOptional = tripsRepo.findById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                TripMember newMember = new TripMember(username, role, status);
                trip.addMember(newMember);

                return tripsRepo.save(trip);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error adding member to trip: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Trips removeMemberFromTrip(String tripId, String username) {
        try {
            Optional<Trips> tripOptional = tripsRepo.findById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();

                Optional<TripMember> memberToRemove = trip.getMembers().stream()
                        .filter(member -> member.getUsername().equals(username))
                        .findFirst();

                if (memberToRemove.isPresent()) {
                    trip.getMembers().remove(memberToRemove.get());

                    return tripsRepo.save(trip);
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Error removing member from trip: {}", e.getMessage(), e);
            throw e;
        }
    }

    public boolean deleteTrip(String id) {
        try {
            tripsRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting trip: {}", e.getMessage(), e);
            return false;
        }
    }
    public Trips changeUserRole(String tripId, String adminUsername, String targetUsername, String newRole) {
        try {
            Optional<Trips> tripOptional = tripsRepo.findById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();

                TripMember adminMember = trip.getMembers().stream()
                        .filter(member -> member.getUsername().equals(adminUsername))
                        .findFirst()
                        .orElse(null);


                if (adminMember != null && TripRole.Organizer.getDescription().equalsIgnoreCase(adminMember.getRole())) {


                    Optional<TripMember> targetMemberOptional = trip.getMembers().stream()
                            .filter(member -> member.getUsername().equals(targetUsername))
                            .findFirst();

                    if (targetMemberOptional.isPresent()) {
                        TripMember targetMember = targetMemberOptional.get();
                        targetMember.setRole(newRole);


                        if (TripRole.Organizer.getDescription().equalsIgnoreCase(newRole) && !adminUsername.equals(targetUsername)) {
                            adminMember.setRole(TripRole.Traveler.getDescription());
                        }

                        return tripsRepo.save(trip);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Error changing user role in trip: {}", e.getMessage(), e);
            return null;
        }
    }
}
