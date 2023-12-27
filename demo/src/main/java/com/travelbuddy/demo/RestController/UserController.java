package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.AdapterClasses.NavbarDTO;
import com.travelbuddy.demo.AdapterClasses.UserProfileDTO;
import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Secruity.ServiceSec.JwtService;
import com.travelbuddy.demo.Services.UserSecService;
import com.travelbuddy.demo.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
            if (user != null) {
                Binary userPictureBinary = user.getPicture();
                byte[] userPictureByteArray;
                if (userPictureBinary != null) {
                    userPictureByteArray = userPictureBinary.getData();
                }else {
                    userPictureByteArray=null;
                }
                    userProfileDTO = new UserProfileDTO(
                            user.getFirstName(), user.getLastName(),
                            userPictureByteArray, user.getPreferences(),
                            user.getTravelDestination(), user.getSocialMediaLinks(),
                            user.getGender(), user.getBirthday(), user.getLocation(),
                            user.getZipCode(),user.getCountry()
                    );
               return ResponseEntity.status(HttpStatus.OK).body(userProfileDTO);
            } else {
                userProfileDTO = new UserProfileDTO("", "", null, "", "", null, "", null, "", "","");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userProfileDTO);
            }

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

    public ResponseEntity<String> createUser(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        try {
            String currentUsername = getCurrentUsername();
            if (currentUsername == null) {
                log.warn("Username must equal the Security username");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username must equal the Security username");
            }


            User user = new User(
                    userProfileDTO.getFirstName(),
                    userProfileDTO.getLastName(),
                    currentUsername,
                    userProfileDTO.getBirthday(),
                    userProfileDTO.getProfilePicture(),
                    userProfileDTO.getPreferences(),
                    userProfileDTO.getTravelDestination(),
                    userProfileDTO.getSocialMediaLinks(),
                    User.Gender.valueOf(userProfileDTO.getGender()),
                    userProfileDTO.getLocation(),
                    userProfileDTO.getZipCode(),
                    userProfileDTO.getCountry()
            );


            userService.saveUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("The User " + user.getUsername() + " were saved");
        } catch (Exception e) {
            log.error("An error occurred while processing the request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");

        }
    }

    @PostMapping("/update")
    @Operation(summary = "Update user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        try {
            log.info("Received request to update user profile: {}", userProfileDTO);

            String currentUsername = getCurrentUsername();

            if (currentUsername == null) {
                log.warn("Username must equal the Security username");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username must equal the Security username");
            }

            User existingUser = userService.findByUsername(currentUsername);

            if (existingUser == null) {
                log.warn("User not found: {}", currentUsername);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + currentUsername);
            }

            existingUser.setFirstName(userProfileDTO.getFirstName());
            existingUser.setLastName(userProfileDTO.getLastName());
            existingUser.setBirthday(userProfileDTO.getBirthday());
            existingUser.setPreferences(userProfileDTO.getPreferences());
            existingUser.setTravelDestination(userProfileDTO.getTravelDestination());
            existingUser.setSocialMediaLinks(userProfileDTO.getSocialMediaLinks());
            User.Gender.valueOf(userProfileDTO.getGender());
            existingUser.setLocation(userProfileDTO.getLocation());
            existingUser.setZipCode(userProfileDTO.getZipCode());
            existingUser.setCountry(userProfileDTO.getCountry());

            byte[] pictureByteArray = userProfileDTO.getProfilePicture();
            if (pictureByteArray != null) {
                Binary pictureBinary = new Binary(pictureByteArray);
                existingUser.setPicture(pictureBinary);
            }

            userService.saveUser(existingUser);

            return ResponseEntity.status(HttpStatus.OK).body("User profile updated successfully");
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
    @GetMapping("/profile-picture")
    @Operation(summary = "Get profile picture by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile picture retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<NavbarDTO> getProfilePictureByUsername() {
        try {
            String currentUsername = getCurrentUsername();
            log.error(getCurrentUsername());
            User user = userService.findByUsername(currentUsername);
            NavbarDTO navbarDTO= new NavbarDTO();
            byte[] userPictureByteArray = null;
            if (user != null) {
                Binary userPictureBinary = user.getPicture();
                if (userPictureBinary != null) {
                    userPictureByteArray = userPictureBinary.getData();
                }}

            if (user.getPicture() == null) {
                log.info("User not found: " + currentUsername);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            navbarDTO.setProfilePicture(userPictureByteArray);
            log.info("Profile picture retrieved for user: " + currentUsername);
            return ResponseEntity.ok(navbarDTO);

        } catch (Exception e) {
            log.error("An error occurred while processing the request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
