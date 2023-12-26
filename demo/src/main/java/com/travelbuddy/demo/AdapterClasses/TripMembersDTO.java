package com.travelbuddy.demo.AdapterClasses;

import com.travelbuddy.demo.AdapterClasses.TripMember;
import java.util.List;

public class TripMembersDTO {

    private List<TripMember> tripMembers;

    // getter and setter
    public List<TripMember> getTripMembers() {
        return tripMembers;
    }

    public void setTripMembers(List<TripMember> tripMembers) {
        this.tripMembers = tripMembers;
    }
}