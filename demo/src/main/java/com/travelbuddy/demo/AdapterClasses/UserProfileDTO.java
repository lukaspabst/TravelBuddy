package com.travelbuddy.demo.AdapterClasses;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

@Getter
@Setter
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String birthday;
    private byte[] profilePicture;
    private String preferences;
    private String travelDestination;
    private Map<String, String> socialMediaLinks;
    private String gender;
    private String location;
    private String zipCode;
    private String country;

    public UserProfileDTO(String firstName, String lastName, byte[] profilePicture, String preferences, String travelDestination, Map<String, String> socialMediaLinks, String gender, String birthday, String location, String zipCode, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = (profilePicture != null) ? profilePicture : new byte[0];
        this.preferences = preferences;
        this.travelDestination = travelDestination;
        this.socialMediaLinks = (socialMediaLinks != null) ? socialMediaLinks : Collections.emptyMap();
        this.gender = gender;
        this.birthday = birthday;
        this.location = location;
        this.zipCode = zipCode;
        this.country=country;
    }
}
