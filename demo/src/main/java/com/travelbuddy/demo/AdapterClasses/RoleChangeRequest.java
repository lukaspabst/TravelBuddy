package com.travelbuddy.demo.AdapterClasses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleChangeRequest {
    @Schema(description = "Ziel nutername", example = "john_doevon", required = true)
    private String targetUsername;
    @Schema(description = "neue Role", example = "Traveler", required = true)
    private String newRole;


    public RoleChangeRequest(String targetUsername, TripRole newRole) {
        this.targetUsername = targetUsername;
        this.newRole = newRole.getDescription();
    }
}
