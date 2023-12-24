package com.travelbuddy.demo.AdapterClasses;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserTripsDto {
    private String id;
    private String nameTrip;
    private Integer maxPersons;
    private String startDate;
    private String endDate;
    private Integer costs;
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

    public UserTripsDto(String id, String nameTrip, Integer maxPersons, String startDate, String endDate, Integer costs) {
        this.id = id;
        this.nameTrip = nameTrip;
        this.maxPersons = maxPersons;
        this.startDate = startDate;
        this.endDate = endDate;
        this.costs = costs;
    }

}
