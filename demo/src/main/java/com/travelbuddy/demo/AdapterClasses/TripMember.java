package com.travelbuddy.demo.AdapterClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripMember {
    private String username;
    private String role;
    private String status;

    public TripMember(String username, TripRole role, String status) {
        this.username = username;
        this.role = role.getDescription();
        this.status = status;
    }
}
