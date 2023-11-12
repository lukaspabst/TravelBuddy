package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.Entities.UserSecurity;
import com.travelbuddy.demo.Exceptions.LoginFailedException;
import com.travelbuddy.demo.Secruity.Infrastructure.AuthenticationResponse;
import com.travelbuddy.demo.Secruity.Infrastructure.LoginRequest;
import com.travelbuddy.demo.Secruity.ServiceSec.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationService service;


    @Operation(summary = "Login to the application")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Parameter(description = "Login request", required = true) @RequestBody LoginRequest request, HttpServletResponse response) throws LoginFailedException {
        try {
            AuthenticationResponse authenticationResponse = service.login(request);
            String jwtToken = authenticationResponse.getJwtToken();
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 24 * 60 * 60);
            response.addCookie(cookie);

            // Set Cache-Control and Pragma headers to prevent caching (Ist nötig, sonst macht der Cache Probleme beim Logout und Reload)
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");

            log.info("User logged in: " + request.getUsername());

            return ResponseEntity.ok().build();
        } catch (LoginFailedException e) {
            log.error("Login failed for user: " + request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@Parameter(description = "User security information", required = true) @Valid @RequestBody UserSecurity userSec, HttpServletResponse response) {
        try {
            AuthenticationResponse authenticationResponse = service.register(userSec);

            String jwtToken = authenticationResponse.getJwtToken();
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 24 * 60 * 60);
            response.addCookie(cookie);

            // Set Cache-Control and Pragma headers to prevent caching (Ist nötig, sonst macht der Cache Probleme beim Logout und Reload)
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");

            log.info("User registered: " + userSec.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            log.error("User registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("User registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Check JWT token validity")
    @ApiResponse(responseCode = "200", description = "JWT token is valid")
    @ApiResponse(responseCode = "401", description = "JWT token is invalid")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/authenticate")
    public ResponseEntity<Void> checkJWT(@CookieValue("jwtToken") String jwtToken) {
        try {
            if (service.checkJwtToken(jwtToken)) {
                log.info("JWT token is valid");
                return ResponseEntity.ok().build();
            } else {
                log.warn("JWT token is invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.error("JWT token validation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Logout and invalidate JWT token")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout( HttpServletResponse response) {
        try {
            Cookie cookie = new Cookie("jwtToken", "");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            log.info("User logged out");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Logout failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
