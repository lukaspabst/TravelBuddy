package com.travelbuddy.demo.Secruity.ServiceSec;

import com.travelbuddy.demo.Entities.UserSecruity;

import com.travelbuddy.demo.Secruity.Infrastructure.AuthenticationResponse;
import com.travelbuddy.demo.Secruity.ShitIdkWohindamit.LoginRequest;
import com.travelbuddy.demo.Services.UserSecService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserSecService userSecService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserSecruity user) {
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

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService
                .generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }

}
