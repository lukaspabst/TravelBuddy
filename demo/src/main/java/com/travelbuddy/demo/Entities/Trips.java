package com.travelbuddy.demo.Entities;

import com.travelbuddy.demo.AdapterClasses.TripMember;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Eindeutige ID des Trips", example = "12345", required = true)
    private String id;
    @Schema(description = "Starttag des Trips", example = "01.01.2024", required = true)
    private String Startdate;
    @Schema(description = "Enddatum des Trips", example = "10.01.2024", required = true)
    private String enddate;
    @Schema(description = "Zielort des Trips", example = "Davids Oarschloch", required = true)
    private String destination;
    @Schema(description = "Beschreibung des Trips", example = "Hier könnte ihre Werbung stehen", required = true)
    private String description;
    @Schema(description = "Kosten des Trips", example = "200000", required = true)
    private Integer costs;
    @Schema(description = "Teilnehmer des Trips", example = "Max Mustermann, Traveler, Active", required = true)
    private List<TripMember> members = new ArrayList<>();
    @Schema(description = "max. Personen des Trips", example = "12", required = true)
    private Integer maxPersons;
    @Schema(description = "Fortbewegungsmittel des Trips", example = "Auto", required = true)
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
