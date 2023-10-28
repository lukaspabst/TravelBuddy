package com.travelbuddy.demo.Secruity.SecPorts;

public interface PasswordEncoderPort {
    String encode(String password);

    boolean matches(String password, String hash);
}
