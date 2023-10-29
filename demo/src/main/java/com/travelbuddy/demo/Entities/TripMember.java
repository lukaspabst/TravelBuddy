package com.travelbuddy.demo.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripMember {
    private String username;
    private String role;
    private String status;

    public TripMember(String username, String role, String status) {
        this.username = username;
        this.role = role;
        this.status = status;
    }
}
