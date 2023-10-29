package com.travelbuddy.demo.Secruity.Infrastructure;

import lombok.extern.slf4j.Slf4j;
import com.travelbuddy.demo.Secruity.SecPorts.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder;
    @Override
    public String encode(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean matches(String password, String hash) {
        try {
            return encoder.matches(password, hash);
        }catch (Exception e){
            log.error("Error while checking password hash: " +e.getMessage());
            return false;
        }
    }
}
