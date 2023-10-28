package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.Entities.User;
import com.travelbuddy.demo.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DatabaseTestService {

    @Autowired
    private UserRepo userRepo; // Assuming you have a UserRepository

    public void testDatabaseConnection() {
        try {
            // Perform a basic database operation, e.g., find a user by their username
            User user = userRepo.findByUsername("exampleUsername");

            // Check if the operation was successful
            if (user != null) {
                System.out.println("Database connection test successful!");
            } else {
                System.err.println("Database operation did not return the expected result.");
            }
        } catch (Exception e) {
            System.err.println("An exception occurred while testing the database connection: " + e.getMessage());
        }
    }
}
