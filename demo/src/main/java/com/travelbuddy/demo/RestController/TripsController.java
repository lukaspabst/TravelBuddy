package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.AdapterClasses.*;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Services.TripsService;
import com.travelbuddy.demo.Services.UserSecService;
import com.travelbuddy.demo.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.travelbuddy.demo.Entities.Trips.mapFromTripsMainContent;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/trips")
public class TripsController {
    @Autowired
    private TripsService tripsService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Create a trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping

    public ResponseEntity<?> createTrip(@Parameter(description = "Trip details", required = true) @Valid @RequestBody TripsMainContent tripsMainContent, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Trips trip = mapFromTripsMainContent(tripsMainContent);
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
    public ResponseEntity<Optional<Trips>> getTrip(@Parameter(description = "Trip ID", required = true) @PathVariable String tripId) {
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

    public ResponseEntity<Trips> updateTrip(@Parameter(description = "Trip ID and Trip Details", required = true) @PathVariable String tripId, @Valid @RequestBody Trips updatedTrip) {
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
            } else {
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
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping("/{tripId}/addMember")
    public ResponseEntity<Trips> addMemberToTrip(@Parameter(description = "Trip ID and New Member", required = true) @PathVariable String tripId, @RequestBody TripMember newMember) {
        try {
            String loggedInUser = getCurrentUsername();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            User user = userService.findByUsername(newMember.getUsername());
            if (user == null) {
                log.warn("User does not exist: {}", newMember.getUsername());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();

                boolean isUserAlreadyMember = trip.getMembers().stream()
                        .anyMatch(member -> member.getUsername().equals(newMember.getUsername()));

                if (isUserAlreadyMember) {
                    log.warn("User is already a member of the trip: {}", newMember.getUsername());
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }

                boolean isAdminOrCoAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) &&
                                (TripRole.Organizer.getDescription().equalsIgnoreCase(member.getRole()) || TripRole.Assistant.getDescription().equalsIgnoreCase(member.getRole())));

                if (isAdminOrCoAdmin) {
                    Trips updatedTrip = tripsService.addMemberToTrip(tripId, newMember.getUsername(), TripRole.valueOf(newMember.getRole()), newMember.getStatus());
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
    public ResponseEntity<Trips> removeMemberFromTrip(@Parameter(description = "Trip ID and username", required = true) @PathVariable String tripId, @PathVariable String username) {
        try {
            String loggedInUser = getCurrentUsername();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdmin = trip.getMembers().stream()
                        .anyMatch(member ->
                                member.getUsername().equals(loggedInUser) &&
                                        TripRole.Organizer.getDescription().equals(member.getRole()));

                boolean isAssistant = trip.getMembers().stream()
                        .anyMatch(member ->
                                member.getUsername().equals(loggedInUser) &&
                                        TripRole.Assistant.getDescription().equals(member.getRole()));

                if (isAdmin || isAssistant) {
                    boolean canRemove = trip.getMembers().stream()
                            .anyMatch(member ->
                                    member.getUsername().equals(username) &&
                                            !(TripRole.Organizer.getDescription().equals(member.getRole()) ||
                                                    (isAssistant && TripRole.Assistant.getDescription().equals(member.getRole()))));

                    if (canRemove) {
                        Trips updatedTrip = tripsService.removeMemberFromTrip(tripId, username);
                        if (updatedTrip != null) {
                            log.info("Removed member from trip with ID: {} by user: {}", tripId, loggedInUser);
                            return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
                        } else {
                            log.error("Error removing member from trip. Trip not updated.");
                            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                        }
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
            String loggedInUser = getCurrentUsername();
            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                boolean isAdmin = trip.getMembers().stream().anyMatch(member ->
                        member.getUsername().equals(loggedInUser) && TripRole.Organizer.getDescription().equals(member.getRole()));

                if (isAdmin) {
                    Trips updatedTrip = tripsService.changeUserRole(
                            tripId,
                            loggedInUser,
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

    @Operation(summary = "Get all trips for the current user that are open")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/userTrips/open")
    public ResponseEntity<List<UserTripsDto>> getUserTripsOpen() {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Trips> userTrips = tripsService.getUserTrips(loggedInUser);
            List<UserTripsDto> userTripsDtoList = userTrips.stream()
                    .filter(trip -> {
                        LocalDate endDate = LocalDate.parse(trip.getEndDate());
                        return endDate.isAfter(LocalDate.now());
                    })
                    .sorted(Comparator.comparing(trip -> LocalDate.parse(trip.getEndDate())))
                    .map(trip -> new UserTripsDto(
                            trip.getId(),
                            trip.getNameTrip(),
                            trip.getMaxPersons(),
                            trip.getStartDate(),
                            trip.getEndDate(),
                            trip.getCosts(),
                            trip.getDestination()
                    ))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(userTripsDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting user trips: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all trips for the current user that are closed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trips retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/userTrips/closed")
    public ResponseEntity<List<UserTripsDto>> getUserTripsClosed() {
        try {
            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Trips> userTrips = tripsService.getUserTrips(loggedInUser);
            List<UserTripsDto> userTripsDtoList = userTrips.stream()
                    .filter(trip -> {
                        LocalDate endDate = LocalDate.parse(trip.getEndDate());
                        return endDate.isBefore(LocalDate.now());
                    })
                    .map(trip -> new UserTripsDto(
                            trip.getId(),
                            trip.getNameTrip(),
                            trip.getMaxPersons(),
                            trip.getStartDate(),
                            trip.getEndDate(),
                            trip.getCosts(),
                            trip.getDestination()
                    ))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(userTripsDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting user trips: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Get the role of the logged-in user for a trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{tripId}/userRole")
    public ResponseEntity<String> getUserRoleForTrip(@Parameter(description = "Trip ID", required = true) @PathVariable String tripId) {
        try {
            String loggedInUser = getCurrentUsername();
            log.debug("Attempting to get user role for user: {} and trip ID: {}", loggedInUser, tripId);

            Optional<Trips> tripOptional = tripsService.getTripById(tripId);

            if (tripOptional.isPresent()) {
                Trips trip = tripOptional.get();
                log.debug("Found trip with ID: {}", tripId);

                Optional<TripMember> userMember = trip.getMembers().stream()
                        .filter(member -> member.getUsername().equals(loggedInUser))
                        .findFirst();

                if (userMember.isPresent()) {
                    String userRole = userMember.get().getRole();
                    log.info("User role for user {} in trip {} is: {}", loggedInUser, tripId, userRole);
                    return new ResponseEntity<>(userRole, HttpStatus.OK);
                } else {
                    log.warn("User {} is not a member of the trip with ID: {}", loggedInUser, tripId);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                log.warn("Trip with ID {} not found.", tripId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error getting user role for trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Get list of usernames in a trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of usernames retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @GetMapping("/{tripId}/members")
    public ResponseEntity<List<SingleTripMemberDTO>> getTripUsernames(
            @Parameter(description = "Trip ID", required = true) @PathVariable String tripId) {
        try {
            Optional<Trips> optionalTrip = tripsService.getTripById(tripId);
            if (optionalTrip.isPresent()) {
                Trips trip = optionalTrip.get();
                List<TripMember> members = trip.getMembers();
                List<SingleTripMemberDTO> listOfSingleTripMember = members.stream().map(member -> {
                    User user = userService.findByUsername(member.getUsername());
                    SingleTripMemberDTO singleTripMember = new SingleTripMemberDTO();
                    singleTripMember.setUsername(user.getUsername());
                    singleTripMember.setName(user.getFirstName()+" "+ user.getLastName());
                    singleTripMember.setRole(member.getRole());
                    byte[] userPictureByteArray = null;
                    if (user != null) {
                        Binary userPictureBinary = user.getPicture();
                        if (userPictureBinary != null) {
                            userPictureByteArray = userPictureBinary.getData();
                        }}
                    singleTripMember.setPicture(userPictureByteArray);
                    return singleTripMember;
                }).collect(Collectors.toList());
                return new ResponseEntity<>(listOfSingleTripMember, HttpStatus.OK);
            } else {
                log.warn("Trip with ID {} not found.", tripId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error getting member details for trip: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

}
