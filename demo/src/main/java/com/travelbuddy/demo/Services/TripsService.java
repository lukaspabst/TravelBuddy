package com.travelbuddy.demo.Services;

import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.AdapterClasses.TripRole;
import com.travelbuddy.demo.AdapterClasses.TripsMainContent;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Repository.TripsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Trips updateTrip(String id, TripsMainContent updatedTrip) {
        try {
            Optional<Trips> existingTripOptional = tripsRepo.findById(id);

            if (existingTripOptional.isPresent()) {
                Trips existingTrip = existingTripOptional.get();
                existingTrip.setNameTrip(updatedTrip.getTripName());
                existingTrip.setCosts(updatedTrip.getCosts());
                existingTrip.setDestination(updatedTrip.getDestination());
                existingTrip.setStartDate(updatedTrip.getStartDate());
                existingTrip.setEndDate(updatedTrip.getEndDate());
                existingTrip.setMaxPersons(updatedTrip.getMaxPersons());

                return saveTrip(existingTrip);

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
    public Trips updateMemberStatus(String tripId, String username, String newStatus) {
        Optional<Trips> optionalTrip = tripsRepo.findById(tripId);

        if (optionalTrip.isPresent()) {
            Trips trip = optionalTrip.get();
            List<TripMember> members = trip.getMembers();

            for (TripMember member : members) {
                if (member.getUsername().equals(username)) {
                    member.setStatus(newStatus);
                    tripsRepo.save(trip); // Save the updated trip
                    return trip;
                }
            }

            // If the member is not found in the trip
            return null;
        }

        // If the trip with the given ID is not found
        return null;
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

    public List<Trips> getUserTrips(String username) {
        try {
            List<Trips> allUserTrips = tripsRepo.findByMembersUsername(username);

            // Filter trips based on the specified member having an active status
            List<Trips> activeUserTrips = allUserTrips.stream()
                    .filter(trip -> trip.getMembers().stream()
                            .anyMatch(member -> username.equals(member.getUsername()) && "Active".equals(member.getStatus())))
                    .collect(Collectors.toList());

            return activeUserTrips;
        } catch (Exception e) {
            log.error("Error getting user trips: {}", e.getMessage(), e);
            throw e;
        }
    }

}
