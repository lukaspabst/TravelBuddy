package com.travelbuddy.demo.Secruity.ShitIdkWohindamit;


import com.travelbuddy.demo.Secruity.SecPorts.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;
    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String hash) {
        return passwordEncoder.matches(password,hash);
    }
}
