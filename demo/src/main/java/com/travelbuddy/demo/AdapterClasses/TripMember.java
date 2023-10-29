package com.travelbuddy.demo.AdapterClasses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripMember {
    @Schema(description = "Benutzername", example = "john_doe", required = true)
    private String username;
    @Schema(description = "Role des Users", example = "Traveler", required = true)
    private String role;
    @Schema(description = "Status des Users", example = "Active", required = true)
    private String status;

    public TripMember(String username, TripRole role, String status) {
        this.username = username;
        this.role = role.getDescription();
        this.status = status;
    }
}
