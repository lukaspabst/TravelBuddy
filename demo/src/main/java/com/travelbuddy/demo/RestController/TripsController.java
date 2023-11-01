package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.AdapterClasses.RoleChangeRequest;
import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.AdapterClasses.TripRole;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Services.TripsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trips")
public class TripsController {
    @Autowired
    private TripsService tripsService;
    @Operation(summary = "Create a trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<Trips> createTrip(@Parameter(description = "Trip details", required = true)@Valid @RequestBody Trips trip) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            TripMember adminMember = new TripMember(username, TripRole.Organizer, "Active");
            trip.addMember(adminMember);

            Trips createdTrip = tripsService.saveTrip(trip);
            log.info("Created a new trip with ID: {} for user: {}", createdTrip.getId(), username);
            return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating a trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Get a trip by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{tripId}")
    public ResponseEntity<Optional<Trips>> getTrip(@Parameter(description = "Trip ID", required = true)@PathVariable String tripId) {
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
    @Operation(summary = "Update a trip by ID and Trips")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip updated successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{tripId}")
    public ResponseEntity<Trips> updateTrip(@Parameter(description = "Trip ID and Trip Details", required = true)@PathVariable String tripId, @Valid @RequestBody Trips updatedTrip) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdminOrCoAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) &&
                                (TripRole.Organizer.getDescription().equalsIgnoreCase(member.getRole()) || TripRole.Assistant.getDescription().equalsIgnoreCase(member.getRole())));

                if (isAdminOrCoAdmin) {
                    updatedTrip.setMembers(trip.getMembers());
                    Trips updated = tripsService.updateTrip(tripId, updatedTrip);

                    if (updated != null) {
                        log.info("Updated trip with ID: {} by user: {}", tripId, loggedInUser);
                        return new ResponseEntity<>(updated, HttpStatus.OK);
                    }
                }
            } else{
                log.warn("No trip found with ID: {} by user: {}", tripId, loggedInUser);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            log.warn("Unauthorized attempt to update trip with ID: {} by user: {}", tripId, loggedInUser);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error("Error updating trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Add a Member by trip ID and newMember")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member added to trip successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping("/{tripId}/addMember")
    public ResponseEntity<Trips> addMemberToTrip(@Parameter(description = "Trip ID and New Member", required = true)@PathVariable String tripId, @RequestBody TripMember newMember) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdminOrCoAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) &&
                                (TripRole.Organizer.getDescription().equalsIgnoreCase(member.getRole()) || TripRole.Assistant.getDescription().equalsIgnoreCase(member.getRole())));

                if (isAdminOrCoAdmin) {
                    Trips updatedTrip = tripsService.addMemberToTrip(tripId, newMember.getUsername(),TripRole.Traveler, newMember.getStatus());
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
    @Operation(summary = "Remove a Member by trip ID and username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member removed from trip successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping("/{tripId}/removeMember/{username}")
    public ResponseEntity<Trips> removeMemberFromTrip(@Parameter(description = "Trip ID and username", required = true)@PathVariable String tripId, @PathVariable String username) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdminOrCoAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) &&
                                (TripRole.Organizer.getDescription().equalsIgnoreCase(member.getRole()) || TripRole.Assistant.getDescription().equalsIgnoreCase(member.getRole())));

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

    @Operation(summary = "Delete by trip ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trip deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@Parameter(description = "Trip ID", required = true) @PathVariable String tripId) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();

                if (trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) && TripRole.Organizer.getDescription().equalsIgnoreCase(member.getRole()))) {
                    boolean deleted = tripsService.deleteTrip(tripId);
                    if (deleted) {
                        log.info("Deleted trip with ID: {}", tripId);
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    } else {
                        log.warn("Error deleting trip with ID: {}", tripId);
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    log.warn("Unauthorized attempt to delete trip with ID: {} by user: {}", tripId, loggedInUser);
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                log.warn("Trip with ID {} not found.", tripId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error deleting trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Change Role by trip ID and RoleChangeRequest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role changed successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping("/{tripId}/changeUserRole")
    public ResponseEntity<Trips> changeUserRole(@Parameter(description = "Trip ID and RoleChangeRequest", required = true)
            @PathVariable String tripId,
            @RequestBody RoleChangeRequest roleChangeRequest) {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) && TripRole.Organizer.getDescription().equalsIgnoreCase(member.getRole()));

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
