package com.travelbuddy.demo.Entities;

import com.travelbuddy.demo.AdapterClasses.TripRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Getter
@Setter
@Document(collection = "userMessage")
public class UserMessage {
    @Id
    private String id;
    @NotNull
    private String type;
    @NotNull
    private String tripId;
    @NotNull
    private String username;
    @NotNull
    private String initiatorUsername;
    @NotNull
    private String nameOfTrip;
    private String roleIfRoleChange;

    public UserMessage(String id, String type, String tripId, String username, String initiatorUsername, String nameOfTrip, String roleIfRoleChange) {
        this.id = id;
        this.type = type;
        this.tripId = tripId;
        this.username = username;
        this.initiatorUsername=initiatorUsername;
        this.nameOfTrip=nameOfTrip;
        if(roleIfRoleChange != null) {
            try {
                if (Arrays.stream(TripRole.values()).anyMatch(role -> role.name().equals(roleIfRoleChange))) {
                    this.roleIfRoleChange = roleIfRoleChange;
                }
            } catch (IllegalArgumentException e) {
                // handle the exception (e.g., log it or throw an error to the caller)
            }
        } else {
            this.roleIfRoleChange = "";
        }
    }
    @Override
    public String toString() {
        return "UserMessage{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", tripId='" + tripId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}