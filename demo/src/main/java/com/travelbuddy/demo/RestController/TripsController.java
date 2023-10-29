package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.Entities.TripMember;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Services.TripsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            Trips createdTrip = tripsService.saveTrip(trip);
            log.info("Created a new trip with ID: {}", createdTrip.getId());
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
            Trips trip = tripsService.updateTrip(tripId, updatedTrip);
            if (trip == null) {
                log.warn("Trip with ID {} not found.", tripId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            log.info("Updated trip with ID: {}", trip.getId());
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{tripId}/addMember")
    public ResponseEntity<Trips> addMemberToTrip(@PathVariable String tripId, @RequestBody TripMember newMember) {
        try {
            Trips trip = tripsService.addMemberToTrip(tripId, newMember.getUsername(), newMember.getRole(), newMember.getStatus());
            if (trip == null) {
                log.warn("Trip with ID {} not found.", tripId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            log.info("Added member to trip with ID: {}", trip.getId());
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error adding member to trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{tripId}/removeMember/{username}")
    public ResponseEntity<Trips> removeMemberFromTrip(@PathVariable String tripId, @PathVariable String username) {
        try {
            Trips trip = tripsService.removeMemberFromTrip(tripId, username);
            if (trip == null) {
                log.warn("Trip with ID {} not found.", tripId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            log.info("Removed member from trip with ID: {}", trip.getId());
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error removing member from trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable String tripId) {
        try {
            boolean deleted = tripsService.deleteTrip(tripId);
            if (deleted) {
                log.info("Deleted trip with ID: {}", tripId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.warn("Trip with ID {} not found.", tripId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error deleting trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
