package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.AdapterClasses.RoleChangeRequest;
import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Services.TripsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trips")
public class TripsController {
    @Autowired
    private TripsService tripsService;

    @PostMapping
    public ResponseEntity<Trips> createTrip(@RequestBody Trips trip) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            TripMember adminMember = new TripMember(username, "Admin", "Active");
            trip.addMember(adminMember);

            Trips createdTrip = tripsService.saveTrip(trip);
            log.info("Created a new trip with ID: {} for user: {}", createdTrip.getId(), username);
            return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating a trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Optional<Trips>> getTrip(@PathVariable String tripId) {
        try {
            Optional<Trips> trip = tripsService.getTripById(tripId);
            if (!trip.isPresent()) {
                log.warn("Trip with ID {} not found.", tripId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting trip details: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trips> updateTrip(@PathVariable String tripId, @RequestBody Trips updatedTrip) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdminOrCoAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) &&
                                ("Admin".equalsIgnoreCase(member.getRole()) || "Co-Admin".equalsIgnoreCase(member.getRole())));

                if (isAdminOrCoAdmin) {
                    Trips updated = tripsService.updateTrip(tripId, updatedTrip);
                    if (updated != null) {
                        log.info("Updated trip with ID: {} by user: {}", tripId, loggedInUser);
                        return new ResponseEntity<>(updated, HttpStatus.OK);
                    }
                }
            }

            log.warn("Unauthorized attempt to update trip with ID: {} by user: {}", tripId, loggedInUser);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("Error updating trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{tripId}/addMember")
    public ResponseEntity<Trips> addMemberToTrip(@PathVariable String tripId, @RequestBody TripMember newMember) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdminOrCoAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) &&
                                ("Admin".equalsIgnoreCase(member.getRole()) || "Co-Admin".equalsIgnoreCase(member.getRole())));

                if (isAdminOrCoAdmin) {
                    Trips updatedTrip = tripsService.addMemberToTrip(tripId, newMember.getUsername(), newMember.getRole(), newMember.getStatus());
                    if (updatedTrip != null) {
                        log.info("Added member to trip with ID: {} by user: {}", tripId, loggedInUser);
                        return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
                    }
                }
            }

            log.warn("Unauthorized attempt to add member to trip with ID: {} by user: {}", tripId, loggedInUser);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("Error adding member to trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/{tripId}/removeMember/{username}")
    public ResponseEntity<Trips> removeMemberFromTrip(@PathVariable String tripId, @PathVariable String username) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdminOrCoAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) &&
                                ("Admin".equalsIgnoreCase(member.getRole()) || "Co-Admin".equalsIgnoreCase(member.getRole())));

                if (isAdminOrCoAdmin) {
                    Trips updatedTrip = tripsService.removeMemberFromTrip(tripId, username);
                    if (updatedTrip != null) {
                        log.info("Removed member from trip with ID: {} by user: {}", tripId, loggedInUser);
                        return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
                    }
                }
            }

            log.warn("Unauthorized attempt to remove member from trip with ID: {} by user: {}", tripId, loggedInUser);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("Error removing member from trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable String tripId) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();

                if (trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) && "Admin".equalsIgnoreCase(member.getRole()))) {
                    boolean deleted = tripsService.deleteTrip(tripId);
                    if (deleted) {
                        log.info("Deleted trip with ID: {}", tripId);
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    } else {
                        log.warn("Trip with ID {} not found.", tripId);
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                }else {
                    log.warn("Unauthorized attempt to delete trip with ID: {} by user: {}", tripId, loggedInUser);
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

            log.warn("Unauthorized attempt to delete trip with ID: {}", tripId);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("Error deleting trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PatchMapping("/{tripId}/changeUserRole")
    public ResponseEntity<Trips> changeUserRole(
            @PathVariable String tripId,
            @RequestBody RoleChangeRequest roleChangeRequest) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) && "Admin".equalsIgnoreCase(member.getRole()));

                if (isAdmin) {
                    Trips updatedTrip = tripsService.changeUserRole(
                            tripId,
                            roleChangeRequest.getAdminUsername(),
                            roleChangeRequest.getTargetUsername(),
                            roleChangeRequest.getNewRole()
                    );
                    if (updatedTrip != null) {
                        log.info("Changed user role in trip with ID: {} by user: {}", tripId, loggedInUser);
                        return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
                    }
                }
            }

            log.warn("Unauthorized attempt to change user role in trip with ID: {} by user: {}", tripId, loggedInUser);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("Error changing user role in trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
