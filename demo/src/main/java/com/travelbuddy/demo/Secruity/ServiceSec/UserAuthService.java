package com.travelbuddy.demo.Secruity.ServiceSec;

import com.travelbuddy.demo.Exceptions.AuthenticationException;
import com.travelbuddy.demo.Secruity.Infrastructure.PasswordEncoderAdapter;
import com.travelbuddy.demo.Secruity.SecPorts.UserAuthPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAuthService implements UserAuthPort {
    private final PasswordEncoderAdapter passwordEncoderAdapter;

    @Autowired
    public UserAuthService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.passwordEncoderAdapter = new PasswordEncoderAdapter(bCryptPasswordEncoder);
    }

    @Override
    public boolean authenticate(String username, String password, String passwordEncoded) {
        try {
            log.info("Authenticating user: " + username);
            boolean isAuthenticated = passwordEncoderAdapter.matches(password, passwordEncoded);
            if (!isAuthenticated) {
                log.error("Authentication failed for user: " + username);
                throw new AuthenticationException("Authentication failed");
            }
            return true;
        } catch (Exception e) {
            log.error("Authentication error for user: " + username, e);
            throw new AuthenticationException("Authentication error");
        }
    }
}
