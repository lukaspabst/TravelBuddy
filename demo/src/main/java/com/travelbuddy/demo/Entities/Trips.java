package com.travelbuddy.demo.Entities;

import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.AdapterClasses.TripsMainContent;
import com.travelbuddy.demo.Validators.DateInFutureOrPresent;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
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

    @Schema(description = "Name des Trips", example = "Ausflug Saufen", required = true)
    @NotBlank(message = "Trip name is mandatory")
    private String nameTrip;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use (yyyy-MM-dd)")
    @DateInFutureOrPresent
    @Schema(description = "Starttag des Trips", example = "01.01.2024", required = true)
    private String startDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use (yyyy-MM-dd)")
    @DateInFutureOrPresent
    @Schema(description = "Enddatum des Trips", example = "10.01.2024", required = true)
    private String endDate;

    @NotBlank(message = "Destination is mandatory")
    @Schema(description = "Zielort des Trips", example = "Davids Oarschloch", required = true)
    private String destination;

    @NotNull
    @Schema(description = "Beschreibung des Trips", example = "Hier könnte ihre Werbung stehen", required = true)
    private String description;

    @Positive(message = "The maximum price must be positive")
    @Schema(description = "Kosten des Trips", example = "200000", required = true)
    private Integer costs;

    @NotNull
    @Schema(description = "Teilnehmer des Trips", example = "Max Mustermann, Traveler, Active", required = true)
    private List<TripMember> members = new ArrayList<>();

    @Min(value = 2, message = "The maximum number of persons should not be less than 2")
    @Max(value = 25, message = "The maximum number of persons should not exceed 25")
    @Schema(description = "max. Personen des Trips", example = "12", required = true)
    private Integer maxPersons;

    @NotNull
    @Schema(description = "Fortbewegungsmittel des Trips", example = "Auto", required = true)
    private String travelVehicle;
    private String type;

    public Trips(List<TripMember> members) {
        this.members = members;
    }

    public Trips(String id, String nameTrip, String startDate, String endDate, String destination, String description, Integer costs, Integer maxPersons, TravelVehicle travelVehicle, String type, List<TripMember> members) {
        if (id == null || startDate == null || endDate == null || destination == null
                || description == null || costs == null || maxPersons == null || travelVehicle == null
                || type == null || members == null || nameTrip == null) {
            throw new IllegalArgumentException("All fields must be provided.");
        }

        this.id = id;
        this.nameTrip = nameTrip;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.description = description;
        this.costs = costs;
        this.maxPersons = maxPersons;
        this.travelVehicle = travelVehicle.getDescription();
        this.type = type;
        this.members = members;
    }

    public Trips(TripsMainContent tripsMainContent) {
        this.nameTrip = tripsMainContent.getTripName();
        this.startDate = tripsMainContent.getStartDate();
        this.endDate = tripsMainContent.getEndDate();
        this.destination = tripsMainContent.getDestination();
        this.costs = tripsMainContent.getCosts();
        this.maxPersons = tripsMainContent.getMaxPersons();
    }

    public static Trips mapFromTripsMainContent(TripsMainContent tripsMainContent) {
        return new Trips(tripsMainContent);
    }

    public void addMember(TripMember member) {
        members.add(member);
    }

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
}
