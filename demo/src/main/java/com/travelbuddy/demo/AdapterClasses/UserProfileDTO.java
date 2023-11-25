package com.travelbuddy.demo.AdapterClasses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String preferences;
    private String travelDestination;
    private String socialMediaLinks;
    private String gender;

    public UserProfileDTO( String firstName, String lastName, String profilePicture, String preferences, String travelDestination, String socialMediaLinks, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.preferences = preferences;
        this.travelDestination = travelDestination;
        this.socialMediaLinks = socialMediaLinks;
        this.gender= gender;
    }
}

