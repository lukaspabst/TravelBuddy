package com.travelbuddy.demo.Entities;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public UserMessage(String id, String type, String tripId, String username, String initiatorUsername, String nameOfTrip) {
        this.id = id;
        this.type = type;
        this.tripId = tripId;
        this.username = username;
        this.initiatorUsername=initiatorUsername;
        this.nameOfTrip=nameOfTrip;
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