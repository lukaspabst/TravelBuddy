package com.travelbuddy.demo.Entities;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Schema(description = "Social Media Links des Benutzers", example = "insta.com/hsohn", required = true)
    Map<String, String> socialMediaLinks;
    @Id
    @Schema(description = "Eindeutige ID des Benutzers", example = "12345", required = true)
    private String id;
    @NotNull
    @Schema(description = "Vorname des Benutzers", example = "Max", required = true)
    private String firstName;
    @NotNull
    @Schema(description = "Nachname des Benutzers", example = "Musterman", required = true)
    private String lastName;
    @NotNull
    @Schema(description = "Unique Username des Benutzers", example = "maxmusterman", required = true)
    @Indexed(unique = true)
    private String username;
    @NotNull
    @Schema(description = "Geburtstag des Benutzers", example = "01.01.2000", required = true)
    private String birthday;
    @Schema(description = "ProfilBild des Benutzers", example = "xxx.com", required = true)
    private Binary picture;
    @NotNull
    @Schema(description = "Bevorzugte Interessen des Benutzers", example = "Wandern", required = true)
    private String preferences;
    @Schema(description = "Reiseziele des Benutzers", example = "Madrid", required = true)
    private String travelDestination;
    @NotNull
    @Schema(description = "Geschlecht des Benutzers", example = "IdentifiziertSichAlsTisch", required = true)
    private String gender;
    @Schema(description = "Wohnort des Benutzers", example = "Berlin", required = true)
    private String location;

    @Schema(description = "ZIP-Code des Benutzers", example = "12345", required = true)
    private String zipCode;

    @Schema(description = "Land des Benutzers", example = "Country", required = true)
    private String country;


    public User(String firstName, String lastName, String username, String bday, byte[] bild, String interests, String reiseziele, Map<String, String> socialMediaLinks, Gender geschlecht, String location, String zipCode, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthday = bday;
        this.picture = new Binary(bild);
        this.preferences = interests;
        this.travelDestination = reiseziele;
        this.socialMediaLinks = socialMediaLinks;
        this.gender = geschlecht.getDescription();
        this.location = location;
        this.zipCode = zipCode;
        this.country = country;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", birthday='" + birthday + '\'' +
                ", picture='" + picture + '\'' +
                ", preferences='" + preferences + '\'' +
                ", travelDestination='" + travelDestination + '\'' +
                ", socialMediaLinks=" + socialMediaLinksToString() +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    private String socialMediaLinksToString() {
        if (socialMediaLinks == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, String> entry : socialMediaLinks.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append('"')
                    .append(entry.getValue())
                    .append('"')
                    .append(", ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");

        return sb.toString();
    }

    public enum Gender {
        M("Male"),
        W("Female"),
        D("Diverse");

        private final String description;

        Gender(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
