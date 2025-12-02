package com.swym.services;

import com.swym.models.AdultRegistrant;
import com.swym.models.ChildRegistrant;
import com.swym.persistence.H2DatabaseAdapter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class RegistrationTest {
    public static void main(String[] args) {
        H2DatabaseAdapter db = new H2DatabaseAdapter();

        // Initialize registration service
        RegistrationService service = new RegistrationService(db);

        // Test Case 1: Standard Adult and Child Registration
        AdultRegistrant adult = new AdultRegistrant(
                "John Doe",
                LocalDate.of(1990, 5, 20),
                "555-1234",
                "emergency1",
                "john345@yyahoo.com", // Unique email
                "None",
                List.of("Basic survival", "Improve stroke"));

        ChildRegistrant child = new ChildRegistrant(
                "Timmy",
                "Mary Doe",
                8,
                "Behavioral issues",
                "Asthma",
                "timmy123@gmail.com"); // Unique email

        // Register them
        try {
            service.registerAdult(adult);
            service.registerChild(child);
        } catch (Exception e) {
            System.err.println("Registration failed for standard test case: " + e.getMessage());
        }

        // Test Case A: Minimal Adult (Stress Test)
        System.out.println("\n--- Test A: Minimal Adult Registration ---");
        AdultRegistrant minimalAdult = new AdultRegistrant(
                "Min Man",
                LocalDate.of(1980, 1, 1),
                "555-9999",
                "Self",
                "min@test.com",
                null,
                List.of("Basic"));
        try {
            service.registerAdult(minimalAdult);
            System.out.println("SUCCESS: Minimal Adult registered.");
        } catch (Exception e) {
            System.err.println("ERROR: Minimal Adult registration failed: " + e.getMessage());
        }

        // Test Case B: Duplicate Email Attempt
        System.out.println("\n--- Test B: Duplicate Registration (Expect Failure) ---");

        boolean failedAsExpected = false;
        try {
            // Attempt to re-register John Doe
            service.registerAdult(adult);

            System.err.println("ERROR: Duplicate registration succeeded when it should have failed.");

        } catch (Exception e) {
            // The exception was thrown
            if (e.getMessage() != null && e.getMessage().contains("violation")) {
                System.out.println("SUCCESS: Duplicate registration correctly prevented by DB integrity.");
                failedAsExpected = true; // Mark test as passed
            } else {
                // Unexpected exception
                System.err.println("ERROR: Test failed with an unexpected exception:");
                e.printStackTrace();
            }
        }

        // This final check ensures that if an exception was NOT thrown, it then logs an
        // error.
        if (!failedAsExpected) {
            System.err.println("CRITICAL ERROR: Test B did not pass. Expected database exception not found.");
        }

        // Print all adults
        System.out.println("\nRegistered Adults:");
        for (AdultRegistrant a : service.getAllAdults()) {
            System.out.println(a.getName() + " | " + a.getEmail() + " | Goals: " + String.join(", ", a.getGoals()));
        }

        // Print all children
        System.out.println("\nRegistered Children:");
        for (ChildRegistrant c : service.getAllChildren()) {
            System.out.println(c.getName() + " | Parent: " + c.getParentName() + " | Age: " + c.getAge());
        }
    }
}