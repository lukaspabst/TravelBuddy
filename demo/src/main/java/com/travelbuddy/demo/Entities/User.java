package com.travelbuddy.demo.Entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    @Schema(description = "Eindeutige ID des Benutzers", example = "12345", required = true)
    private String id;
    @Schema(description = "Vorname des Benutzers", example = "Max", required = true)
    private String firstName;
    @Schema(description = "Nachname des Benutzers", example = "Musterman", required = true)
    private String lastName;
    @Schema(description = "Unique Username des Benutzers", example = "maxmusterman", required = true)
    @Indexed(unique = true)
    private String username;
    @Schema(description = "Geburtstag des Benutzers", example = "01.01.2000", required = true)

    private String birthday;
    @Schema(description = "Link zum ProfilBild des Benutzers", example = "xxx.com", required = true)
    private String picture;
    @Schema(description = "Bevorzugte Interessen des Benutzers", example = "Wandern", required = true)
    private String preferences;
    @Schema(description = "Reiseziele des Benutzers", example = "Madrid", required = true)
    private String travelDestination;
    @Schema(description = "Social Media Links des Benutzers", example = "insta.com/hsohn", required = true)
    private String socialMediaLinks;
    @Schema(description = "Geschlecht des Benutzers", example = "IdentifiziertSichAlsTisch", required = true)
    private String gender;


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
    public User(String firstName, String lastName, String username, String bday, String bild, String interests, String reiseziele, String socialMediaLinks, Gender geschlecht) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthday = bday;
        this.picture = bild;
        this.preferences = interests;
        this.travelDestination = reiseziele;
        this.socialMediaLinks = socialMediaLinks;
        this.gender = geschlecht.getDescription();
    }
}
