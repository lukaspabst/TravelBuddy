package com.travelbuddy.demo.AdapterClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleChangeRequest {
    private String adminUsername;
    private String targetUsername;
    private String newRole;

    public RoleChangeRequest(String adminUsername, String targetUsername, String newRole) {
        this.adminUsername = adminUsername;
        this.targetUsername = targetUsername;
        this.newRole = newRole;
    }
}
