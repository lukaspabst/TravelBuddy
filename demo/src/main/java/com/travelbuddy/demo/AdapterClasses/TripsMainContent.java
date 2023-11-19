package com.travelbuddy.demo.AdapterClasses;

import com.travelbuddy.demo.Entities.Trips;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TripsMainContent {
    private String tripName;
    private String startDate;
    private String endDate;
    private String destination;
    private Integer costs;
    private Integer maxPersons;

    public TripsMainContent(String tripName, String startdate, String enddate, String destination, Integer costs, Integer maxPersons) {
        this.tripName = tripName;
        this.startDate = startdate;
        this.endDate = enddate;
        this.destination = destination;
        this.costs = costs;
        this.maxPersons = maxPersons;
    }

    public TripsMainContent(Trips trips) {
        this.tripName = trips.getNameTrip();
        this.startDate = trips.getStartdate();
        this.endDate = trips.getEnddate();
        this.destination = trips.getDestination();
        this.costs = trips.getCosts();
        this.maxPersons = trips.getMaxPersons();
    }

}
