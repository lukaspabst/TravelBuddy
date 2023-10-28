package com.travelbuddy.demo.Services;

import com.travelbuddy.demo.Entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleDataService {

    @Autowired
    private final UserService userService;

    @PostMapping("/addExample")
    public void saveExampleData() {
        // Create example User objects and save them to the database
        User user1 = new User("John", "Doe", "johndoe", "1990-01-15", "profile1.jpg", "Travel", "Europe", "example.com/johndoe", "m");
        User user2 = new User("Jane", "Smith", "janesmith", "1988-03-20", "profile2.jpg", "Hiking", "Asia", "example.com/janesmith", "f");

        userService.saveUser(user1);
        userService.saveUser(user2);

        System.out.println("Example data saved to the database.");
    }
}
