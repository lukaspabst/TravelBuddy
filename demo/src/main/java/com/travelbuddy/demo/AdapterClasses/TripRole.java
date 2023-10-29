package com.travelbuddy.demo.AdapterClasses;

public enum TripRole {

    Organizer("Trip Organizer"),
    Assistant("Trip Assistant"),
    Traveler("Traveler");

    private final String description;

    TripRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
