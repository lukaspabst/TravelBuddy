package com.travelbuddy.demo.Entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@Document(collection = "trips")
public class Trips {
    @Id
    private String id;

    private String Startdate;

    private String enddate;

    private String destination;

    private String description;

    private Integer costs;

    private List<TripMember> members = new ArrayList<>();

    private Integer maxPersons;

    private String travelVehicle;

    public Trips(List<TripMember> members) {
        this.members = members;
    }

    public Trips(String id, String startdate, String enddate, String destination, String description, Integer costs, Integer maxPersons, TravelVehicle travelVehicle, String type,List<TripMember> members) {
        this.id = id;
        Startdate = startdate;
        this.enddate = enddate;
        this.destination = destination;
        this.description = description;
        this.costs = costs;
        this.maxPersons = maxPersons;
        this.travelVehicle = travelVehicle.getDescription();
        this.type = type;
        this.members = members;
    }

    private String type;


    public enum TravelVehicle {
        CAR("Car"),
        AIRCRAFT("aircraft"),
        BUS("bus"),
        TRAIN("train"),
        NONE("none");
        private final String description;

        TravelVehicle(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
    public void addMember(TripMember member) {
        members.add(member);
    }
}
