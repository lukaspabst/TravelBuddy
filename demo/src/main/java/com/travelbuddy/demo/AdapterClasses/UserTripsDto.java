package com.travelbuddy.demo.AdapterClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTripsDto {
    private String id;
    private String nameTrip;
    private Integer maxPersons;
    private String startdate;
    private String enddate;

    public UserTripsDto(String id, String nameTrip, Integer maxPersons, String startdate, String enddate) {
        this.id = id;
        this.nameTrip = nameTrip;
        this.maxPersons = maxPersons;
        this.startdate = startdate;
        this.enddate = enddate;
    }

}
