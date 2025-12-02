package com.swym.services;

import com.swym.persistence.H2DatabaseAdapter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProgressManagerTest {
    public static void main(String[] args) {
        H2DatabaseAdapter db = new H2DatabaseAdapter();

        // Clear progress_records table before testing
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM progress_records");
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        // Assign stage
        String result = pm.assignStage("inst1", "student1", 3, "Initial stage");
        System.out.println(result);

        // Check current stage
        int currentStage = pm.getCurrentStage("student1");
        System.out.println("Current stage: " + currentStage);
    }
}
