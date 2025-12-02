package com.swym.services;

import com.swym.persistence.H2DatabaseAdapter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProgressManagerTest {
    public static void main(String[] args) {
        H2DatabaseAdapter db = new H2DatabaseAdapter();

        // Access control stub that allows everything
        AccessControl ac = new AccessControl() {
            @Override
            public boolean hasInstructorPrivileges(String userId) {
                return true;
            }

            @Override
            public boolean hasReportAccess(String userId) {
                return true;
            }
        };

        ProgressManager pm = new ProgressManager(db, ac);

        // Standard Test Case: Assign initial stage to a new student
        String standardStudent = "student1";
        String result = pm.assignStage("inst1", standardStudent, 3, "Initial stage");
        System.out.println(result);

        int currentStage = pm.getCurrentStage(standardStudent);
        System.out.println("Current stage: " + currentStage);

        // CLEAN UP THE TEMPORARY STUDENT IMMEDIATELY
        try (Connection conn = db.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM progress_records WHERE studentId = '" + standardStudent + "'");
        } catch (SQLException e) {
            System.err.println("Error cleaning temporary student record.");
        }

        // Test Case C: Stage Progression (Alice)
        // Alice is at Stage 5 from injected data. We test moving her to Stage 6.
        String studentIdProg = "alice@swym.com";

        System.out.println("\n--- Test C: Stage Progression ---");
        int initialStage = pm.getCurrentStage(studentIdProg);

        // This call INSERTS a new record (Stage 6) with a new, later timestamp.
        String result5to6 = pm.assignStage("inst1", studentIdProg, 6, "Mastered freestyle.");
        int finalStage = pm.getCurrentStage(studentIdProg);

        System.out.println("Initial Stage for Alice: " + initialStage); // Should be 5
        System.out.println("New Stage for Alice: " + finalStage); // Should be 6

        if (finalStage > initialStage) {
            System.out.println("SUCCESS: Stage progression validated.");
        }

        // Test Case D: Invalid Stage (Negative Test)
        String studentIdInvalid = "ben@swym.com";

        System.out.println("\n--- Test D: Invalid Stage Assignment ---");
        String resultInvalid = pm.assignStage("inst1", studentIdInvalid, -1, "Impossible stage.");

        if (resultInvalid.contains("ERROR")) {
            System.out.println("SUCCESS: Invalid stage assignment correctly blocked.");
        } else {
            System.err.println("ERROR: Invalid stage assignment was accepted.");
        }

    }
}