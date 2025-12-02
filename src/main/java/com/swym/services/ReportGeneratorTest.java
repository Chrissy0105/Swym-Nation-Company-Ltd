package com.swym.services;

import com.swym.models.ProgressRecord;
import com.swym.persistence.H2DatabaseAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class ReportGeneratorTest {
    public static void main(String[] args) {
        // 1. Setup database and access control
        H2DatabaseAdapter db = new H2DatabaseAdapter();

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

        // 2. Insert sample progress records with timestamps
        ProgressRecord r1 = new ProgressRecord(
                "r1", "student1", "inst1", "classA", 2, "Stage 2 complete",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1));

        ProgressRecord r2 = new ProgressRecord(
                "r2", "student2", "inst1", "classA", 3, "Stage 3 complete",
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2));

        ProgressRecord r3 = new ProgressRecord(
                "r3", "student3", "inst2", "classB", 1, "Stage 1 complete",
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3));

        db.insertProgressRecord(r1);
        db.insertProgressRecord(r2);
        db.insertProgressRecord(r3);

        db.updateStudentStage("student1", 2);
        db.updateStudentStage("student2", 3);
        db.updateStudentStage("student3", 1);

        // 3. Generate report
        ReportGenerator rg = new ReportGenerator(db, ac);
        ReportFilter filter = new ReportFilter();
        filter.setStart(LocalDate.now().minusDays(7));
        filter.setEnd(LocalDate.now());
        filter.setStudentId("student1");
        filter.setInstructorId("inst1");
        filter.setClassId("classA");

        Report report = rg.generateReport("admin1", filter);

        // 4. Print results
        System.out.println("Report Title: " + report.getTitle());
        System.out.println("Number of rows: " + report.getRows().size());

        for (Map<String, Object> row : report.getRows()) {
            System.out.println("Row: "
                    + row.get("studentId") + " | Stage "
                    + row.get("stage") + " | "
                    + row.get("notes"));
        }

        System.out.println(rg.exportCSV(report, "report.csv"));
        System.out.println(rg.exportPDF(report, "report.pdf"));
    }
}
