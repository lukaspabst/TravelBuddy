package com.travelbuddy.demo.Secruity.ServiceSec;

import com.travelbuddy.demo.Entities.UserSecurity;
import com.travelbuddy.demo.Exceptions.LoginFailedException;
import com.travelbuddy.demo.Secruity.Infrastructure.AuthenticationResponse;
import com.travelbuddy.demo.Secruity.Infrastructure.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) throws LoginFailedException {
        return ResponseEntity.ok(service.login(request));
    }
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserSecurity userSec) {
        service.register(userSec);
        return ResponseEntity.ok(userSec.getUsername());
    }
}
