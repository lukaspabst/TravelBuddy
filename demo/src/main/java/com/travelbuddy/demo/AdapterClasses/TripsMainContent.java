package com.travelbuddy.demo.AdapterClasses;

import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Validators.DateInFutureOrPresent;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class TripsMainContent {
    private String tripName;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use (yyyy-MM-dd)")
    @DateInFutureOrPresent
    private String startDate;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use (yyyy-MM-dd)")
    @DateInFutureOrPresent
    private String endDate;
    private String destination;
    private Integer costs;
    private Integer maxPersons;
    @AssertTrue(message = "Start date must be before end date")
    public boolean isDateValid() {
        LocalDate startDateParsed;
        LocalDate endDateParsed;

        try {
            startDateParsed = LocalDate.parse(this.getStartDate());
            endDateParsed = LocalDate.parse(this.getEndDate());
        } catch(Exception e) {
            return false;
        }

        return !startDateParsed.isAfter(endDateParsed);
    }
    public TripsMainContent(String tripName, String startDate, String endDate, String destination, Integer costs, Integer maxPersons) {
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.costs = costs;
        this.maxPersons = maxPersons;
    }

    public TripsMainContent(Trips trips) {
        this.tripName = trips.getNameTrip();
        this.startDate = trips.getStartDate();
        this.endDate = trips.getEndDate();
        this.destination = trips.getDestination();
        this.costs = trips.getCosts();
        this.maxPersons = trips.getMaxPersons();
    }

}
