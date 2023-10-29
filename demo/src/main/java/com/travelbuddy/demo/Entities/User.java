package com.travelbuddy.demo.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String username;
    private String bday;
    private String bild; // Sie können eine URL oder einen Dateiverweis für Bilder verwenden
    private String interests;
    private String reiseziele;
    private String socialMediaLinks;
    private Geschlecht geschlecht; // Use the Geschlecht enum for gender

    // Other fields, constructors, and methods

    public enum Geschlecht {
        M("Male"),
        W("Female"),
        D("Diverse");

        private final String description;

        Geschlecht(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
    public User(String firstName, String lastName, String username, String bday, String bild, String interests, String reiseziele, String socialMediaLinks, Geschlecht geschlecht) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.bday = bday;
        this.bild = bild;
        this.interests = interests;
        this.reiseziele = reiseziele;
        this.socialMediaLinks = socialMediaLinks;
        this.geschlecht = geschlecht;
    }
}
