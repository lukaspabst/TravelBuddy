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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    @Operation(summary = "Login to the application")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Parameter(description = "Login request", required = true)@RequestBody LoginRequest request, HttpServletResponse response) throws LoginFailedException {
        String jwtToken = service.login(request).getJwtToken();

        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/register")

    public ResponseEntity<String> createUser(@Parameter(description ="User security information", required = true)@Valid @RequestBody UserSecurity userSec) {
        service.register(userSec);
        return ResponseEntity.ok(userSec.getUsername());
    }
}
