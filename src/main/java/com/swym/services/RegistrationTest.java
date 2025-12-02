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

        // Clear tables for a clean test run
        try (Connection conn = db.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM adults");
            stmt.executeUpdate("DELETE FROM children");
            stmt.executeUpdate("DELETE FROM progress_records");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Initialize registration service
        RegistrationService service = new RegistrationService(db);

        // Create sample AdultRegistrant
        AdultRegistrant adult = new AdultRegistrant(
                "John Doe",
                LocalDate.of(1990, 5, 20),
                "555-1234",
                "emergency1",
                "john@example.com",
                "None",
                List.of("Basic survival", "Improve stroke"));

        // Create sample ChildRegistrant
        ChildRegistrant child = new ChildRegistrant(
                "Timmy",
                "Mary Doe",
                8,
                "Behavioral issues",
                "Asthma",
                "timmy@example.com");

        // Register them
        service.registerAdult(adult);
        service.registerChild(child);

        // Print all adults
        System.out.println("Registered Adults:");
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
