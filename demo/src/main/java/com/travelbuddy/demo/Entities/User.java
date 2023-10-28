package com.travelbuddy.demo.Entities;

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
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String username;

    private String birthday;

    private String picture;
    private String preferences;
    private String travelDestination;
    private String socialMediaLinks;
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
