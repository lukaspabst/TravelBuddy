package com.travelbuddy.demo.Secruity.SecPorts;

public interface UserAuthPort {

    boolean authenticate(String username, String password, String passwordEncoded);

}
