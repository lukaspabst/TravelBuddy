package com.travelbuddy.demo.Secruity.ServiceSec;

import com.travelbuddy.demo.Entities.UserSecruity;
import com.travelbuddy.demo.Secruity.Infrastructure.AuthenticationResponse;
import com.travelbuddy.demo.Secruity.ShitIdkWohindamit.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(service.login(request));
    }
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserSecruity userSec) {
        service.register(userSec);
        return ResponseEntity.ok(userSec.getUsername());
    }
}
