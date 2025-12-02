package com.swym.services;

import com.swym.persistence.H2DatabaseAdapter;
import java.util.Map;
import java.io.File;
import java.io.IOException;

public class ReportGeneratorTest {
    public static void main(String[] args) {
        // Initialize DB and Report Generator
        H2DatabaseAdapter db = new H2DatabaseAdapter();

        // Access control stub
        AccessControl ac = new AccessControl() {
            public boolean hasInstructorPrivileges(String userId) {
                return true;
            }

            public boolean hasReportAccess(String userId) {
                return true;
            }
        };
        ReportGenerator rg = new ReportGenerator(db, ac);

        // TEST 1: Check Total Rows and Basic Data Retrieval
        ReportFilter filter = new ReportFilter();
        Report report = rg.generateReport("admin", filter);

        System.out.println("TEST 1: Full Report Row Count");
        int expectedRows = 5;
        System.out.println("Expected Rows: " + expectedRows + " | Actual Rows: " + report.getRows().size());

        if (report.getRows().size() != expectedRows) {
            System.err.println("ERROR: Row count mismatch. The database view is not returning the expected 5 records.");

            // Debugging
            System.out.println("--- DIAGNOSTIC: Records Found (Actual: " + report.getRows().size() + ") ---");
            for (Map<String, Object> row : report.getRows()) {
                System.out.println("Found Record for Student ID: " + row.get("STUDENT_ID") + " (Name: "
                        + row.get("STUDENT_NAME") + ", Stage: " + row.get("STAGE") + ")");
            }
            System.out.println("------------------------------------------");
        } else {
            System.out.println("SUCCESS: Row count matches expected records.");
        }

        // TEST 2: Verify Alice Smith's Latest Progress

        ReportFilter filterAlice = new ReportFilter();
        filterAlice.setStudentId("alice@swym.com");
        Report aliceReport = rg.generateReport("admin", filterAlice);

        System.out.println("\nTEST 2: Alice Smith's Latest Progress Verification");

        if (aliceReport.getRows().size() == 1) {
            Map<String, Object> latestRecord = aliceReport.getRows().get(0); // Only record returned

            String actualName = (String) latestRecord.get("STUDENT_NAME");
            Integer actualStage = (Integer) latestRecord.get("STAGE");

            System.out.println("Student Name: " + actualName);
            System.out.println("Latest Stage: " + actualStage);

            if (!"Alice Smith".equals(actualName)) {
                System.err.println("ERROR: Name resolution failed. Expected 'Alice Smith'.");
            } else if (actualStage == null || actualStage.intValue() != 6) {
                System.err.println("ERROR: Stage retrieval failed. Expected stage 6 (post-test).");
            } else {
                System.out.println("SUCCESS: Alice's name resolved and latest stage confirmed.");
            }
        } else {
            System.err
                    .println("ERROR: Alice Smith's records not found in the report or multiple records found (Actual: "
                            + aliceReport.getRows().size() + ")");
        }

        // TEST 3: CSV Export Verification
        System.out.println("\n--- TEST 3: CSV Export Verification ---");
        String csvFileName = "test_report.csv";
        File csvFile = new File(csvFileName);

        try {
            rg.exportToCSV(report, csvFileName);

            if (csvFile.exists() && csvFile.length() > 0) {
                System.out.println("SUCCESS: CSV file created at: " + csvFile.getAbsolutePath());
                System.out.println("File size (bytes): " + csvFile.length());
            } else {
                System.err.println("ERROR: CSV file either not created or is empty.");
            }
        } catch (IOException e) {
            System.err.println("ERROR: CSV export failed with exception: " + e.getMessage());
        } finally {
            if (csvFile.exists()) {
                // Uncomment whenever I don't want to keep the files
            }
        }

        // TEST 4: PDF Export Verification
        System.out.println("\n--- TEST 4: PDF Export Verification ---");
        String pdfFileName = "test_report.pdf";
        File pdfFile = new File(pdfFileName);

        try {
            rg.exportToPDF(report, pdfFileName);

            if (pdfFile.exists() && pdfFile.length() > 0) {
                System.out.println("SUCCESS: PDF file created at: " + pdfFile.getAbsolutePath());
                System.out.println("File size (bytes): " + pdfFile.length());
            } else {
                System.err.println("ERROR: PDF file either not created or is empty.");
            }
        } catch (Exception e) {
            System.err.println("ERROR: PDF export failed with exception: " + e.getMessage());
        } finally {
            if (pdfFile.exists()) {
                // Uncomment whenever I don't want to keep the files
            }
        }
    }
}