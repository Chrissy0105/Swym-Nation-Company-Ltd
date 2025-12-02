package com.swym.services;

import com.swym.models.ProgressRecord;
import com.swym.persistence.H2DatabaseAdapter;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestProgressDataInjector {
    // real test data updates
    private static final LocalDateTime OLD_TIMESTAMP = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

    public static void inject(H2DatabaseAdapter db) {
        // 1. Alice Smith (Adult) - Expected Stage 5
        insertProgressRecordIfMissing(db, "alice@swym.com", 5);

        // 2. Ben Taylor (Adult) - Expected Stage 4
        insertProgressRecordIfMissing(db, "ben@swym.com", 4);

        // 3. Charlie Green (Child) - Expected Stage 2
        insertProgressRecordIfMissing(db, "charlie@swym.com", 2);

        // 4. Dana White (Child) - Expected Stage 3
        insertProgressRecordIfMissing(db, "dana@swym.com", 3);

        // 5. Eve Black (Child) - Expected Stage 1
        insertProgressRecordIfMissing(db, "eve@swym.com", 1);
    }

    private static void insertProgressRecordIfMissing(H2DatabaseAdapter db, String studentId, int stage) {
        // Check if a record already exists for the student
        if (db.getStudentCurrentStage(studentId) == 0) {
            db.insertProgressRecord(new ProgressRecord(
                    UUID.randomUUID().toString(),
                    studentId,
                    "INJECTOR",
                    "TEST_CLASS",
                    stage,
                    "Initial data injection point.",
                    OLD_TIMESTAMP, // Using a definite past timestamp
                    OLD_TIMESTAMP));
        }
    }
}