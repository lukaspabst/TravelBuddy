package com.travelbuddy.demo.Secruity.ServiceSec;

import com.travelbuddy.demo.Entities.UserSecurity;
import com.travelbuddy.demo.Exceptions.LoginFailedException;
import com.travelbuddy.demo.Secruity.Infrastructure.AuthenticationResponse;
import com.travelbuddy.demo.Secruity.Infrastructure.LoginRequest;
import com.travelbuddy.demo.Secruity.SecPorts.UserAuthPort;
import com.travelbuddy.demo.Services.UserSecService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserSecService userSecService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;

    private final UserAuthPort authenticationManager;

    public AuthenticationResponse register(UserSecurity user) {
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userSecService.saveUser(user);
        var jwtToken = jwtService
                .generateToken(user.getUsername());
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) throws LoginFailedException {
        try {
            Optional<UserSecurity> userSec = userSecService.findByUsername(request.getUsername());
            if (userSec.isPresent()) {
                if (userSec.get().isNotLocked()) {
                    if (authenticationManager.authenticate(request.getUsername(), request.getPassword(), userSec.get().getPassword())) {
                        var jwtToken = jwtService
                                .generateToken(request.getUsername());
                        return AuthenticationResponse.builder()
                                .jwtToken(jwtToken)
                                .build();
                    } else {
                        throw new LoginFailedException("Authentication failed. Please check your credentials.");
                    }
                } else {
                    throw new LoginFailedException("User account is locked. Please contact support.");
                }
            } else {
                throw new UsernameNotFoundException("User not found.");
            }
        } catch (UsernameNotFoundException e) {
            log.error("Username not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            throw new LoginFailedException("Login failed. Please try again.");
        }
    }

    public boolean checkJwtToken(String jwtToken) {
        return jwtService.isTokenValid(jwtToken, jwtService.extractUsername(jwtToken));
    }
}
