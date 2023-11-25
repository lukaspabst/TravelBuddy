package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.AdapterClasses.UserProfileDTO;
import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Secruity.ServiceSec.AuthenticationService;
import com.travelbuddy.demo.Secruity.ServiceSec.JwtService;
import com.travelbuddy.demo.Services.UserSecService;
import com.travelbuddy.demo.Services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserSecService userSecService;

    private final JwtService service;
    @GetMapping("/profile")
    @Operation(summary = "Get the current user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile found"),
            @ApiResponse(responseCode = "404", description = "User profile not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
        try {

            String currentUsername = getCurrentUsername();
            log.error(getCurrentUsername());
            User user = userService.findByUsername(currentUsername);
            UserProfileDTO userProfileDTO;
            if(user != null) {
                 userProfileDTO = new UserProfileDTO( user.getFirstName(), user.getLastName(), user.getPicture(), user.getPreferences(), user.getTravelDestination(), user.getSocialMediaLinks(), user.getGender());
            }else{
                 userProfileDTO= new UserProfileDTO("","","","","","","");
            }
            return ResponseEntity.ok(userProfileDTO);
        } catch (Exception e) {
            log.error("An error occurred while processing the request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register")

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
  
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        try {
            if (!user.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {

                log.warn("Username must equal the Security username");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username must equal the Security username");
            }
            User savedUser = userService.saveUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("The User " + user.getUsername() + " were saved");
        } catch (Exception e) {
            log.error("An error occurred while processing the request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");

        }
    }

    @DeleteMapping("/{username}")

    @Operation(summary = "Delete a user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUser = authentication.getName();

            if (!loggedInUser.equals(username)) {
                log.warn("Unauthorized access attempt by user: " + loggedInUser);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access attempt by user: " + loggedInUser);
            }

            userSecService.deleteUserByUsername(username);

            if (userService.findByUsername(username) != null) {

                userService.deleteUserByUsername(username);
            }

            log.info("User deleted: " + username);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("An error occurred while processing the request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");

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
