package com.swym.services;

import com.swym.models.AdultRegistrant;
import com.swym.models.ChildRegistrant;
import com.swym.models.ProgressRecord;
import com.swym.persistence.H2DatabaseAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DataInitializer {

        // Define a guaranteed old timestamp (Jan 1, 2000)
        private static final LocalDateTime OLD_TIMESTAMP = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

        public static void main(String[] args) {
                try {
                        // method to ensure auto-commit is enabled for every insert.
                        H2DatabaseAdapter db = new H2DatabaseAdapter();
                        initializeDatabase(db);
                } catch (Exception e) {
                        System.err.println("FATAL ERROR: Database initialization failed.");
                        e.printStackTrace();
                        System.exit(1);
                }
        }

        public static void initializeDatabase(H2DatabaseAdapter db) throws Exception {
                // 1. Register Initial Adult Students
                db.insertAdult(new AdultRegistrant(
                                "Alice Smith",
                                LocalDate.of(1995, 3, 15),
                                "111-1111",
                                "EmA",
                                "alice@swym.com",
                                "None",
                                List.of("Endurance", "Freestyle")));

                db.insertAdult(new AdultRegistrant(
                                "Ben Taylor",
                                LocalDate.of(1988, 7, 22),
                                "222-2222",
                                "EmB",
                                "ben@swym.com",
                                "Backstroke",
                                List.of("Water Safety")));

                // 2. Register Initial Child Students
                db.insertChild(new ChildRegistrant("Charlie Green", "Parent C", 6, "Calm", "None", "charlie@swym.com"));
                db.insertChild(new ChildRegistrant("Dana White", "Parent D", 8, "Energetic", "Allergies",
                                "dana@swym.com"));
                db.insertChild(new ChildRegistrant("Eve Black", "Parent E", 4, "Shy", "None", "eve@swym.com"));

                // 3. Inject Progress Records Directly
                // These inserts guarantee the ReportGeneratorTest finds 5 records.

                db.insertProgressRecord(new ProgressRecord(
                                UUID.randomUUID().toString(), "alice@swym.com", "INJECTOR", "TEST_CLASS", 5,
                                "Initial data injection point.", OLD_TIMESTAMP, OLD_TIMESTAMP));

                db.insertProgressRecord(new ProgressRecord(
                                UUID.randomUUID().toString(), "ben@swym.com", "INJECTOR", "TEST_CLASS", 4,
                                "Initial data injection point.", OLD_TIMESTAMP, OLD_TIMESTAMP));

                db.insertProgressRecord(new ProgressRecord(
                                UUID.randomUUID().toString(), "charlie@swym.com", "INJECTOR", "TEST_CLASS", 2,
                                "Initial data injection point.", OLD_TIMESTAMP, OLD_TIMESTAMP));

                db.insertProgressRecord(new ProgressRecord(
                                UUID.randomUUID().toString(), "dana@swym.com", "INJECTOR", "TEST_CLASS", 3,
                                "Initial data injection point.", OLD_TIMESTAMP, OLD_TIMESTAMP));

                db.insertProgressRecord(new ProgressRecord(
                                UUID.randomUUID().toString(), "eve@swym.com", "INJECTOR", "TEST_CLASS", 1,
                                "Initial data injection point.", OLD_TIMESTAMP, OLD_TIMESTAMP));

                System.out.println("Data injection complete.");
        }
}