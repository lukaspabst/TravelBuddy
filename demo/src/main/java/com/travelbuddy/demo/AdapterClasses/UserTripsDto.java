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
    private String startdate;
    private String enddate;
    @AssertTrue(message = "Start date must be before end date")
    public boolean isDateValid() {
        LocalDate startDateParsed;
        LocalDate endDateParsed;

        try {
            startDateParsed = LocalDate.parse(this.getStartdate());
            endDateParsed = LocalDate.parse(this.getEnddate());
        } catch(Exception e) {
            return false;
        }

        return !startDateParsed.isAfter(endDateParsed);
    }

    public UserTripsDto(String id, String nameTrip, Integer maxPersons, String startdate, String enddate) {
        this.id = id;
        this.nameTrip = nameTrip;
        this.maxPersons = maxPersons;
        this.startdate = startdate;
        this.enddate = enddate;
    }

}
