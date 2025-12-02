-- *** Adults ***
INSERT INTO adults (name, dob, contactNumber, emergencyContact, email, medicalInfo, goals) VALUES
('Alice Smith', '1985-07-25', '555-1001', 'John Smith (555-1002)', 'alice@swym.com', 'None', 'Endurance,Freestyle'),
('Ben Taylor', '1998-01-10', '555-1003', 'Self', 'ben@swym.com', 'Mild Asthma', 'Water Safety'),
('Jane Doe', '1990-05-15', '555-1004', 'Husband (555-1005)', 'jane.doe@example.com', 'Knee surgery in 2020', 'Improve stroke,Water aerobics');

-- *** Children ***
INSERT INTO children (childName, parentName, age, behaviorNotes, illnesses, email) VALUES
('Charlie Green', 'Parent C', 6, 'Needs constant encouragement', 'Allergies', 'charlie@swym.com'),
('Dana White', 'Parent D', 8, 'Very energetic, learns fast', 'None', 'dana@swym.com'),
('Eve Black', 'Parent E', 4, 'Shy, easily distracted', 'Eczema', 'eve@swym.com'),
('Timmy', 'Mary Doe', 8, 'Very polite', 'None', 'timmy@swym.com');


-- *** Progress Records ***
-- Ensure all 5 students used in the report test have a single, latest record.
-- The timestamps are distinct and ordered to clearly define the "latest" record.
INSERT INTO progress_records (id, studentId, instructorId, classId, stage, notes, createdAt, updatedAt) VALUES
-- Alice Smith: Latest Stage 5 (Crucial Fix: Use only one record to avoid ambiguity with the view)
('P_A_LATEST', 'alice@swym.com', 'Inst_501', 'Class_B', 5, 'Stronger treading.', '2025-11-25 10:00:00', '2025-11-25 10:00:00'),

-- Ben Taylor: Latest Stage 4 (Previously reported in test output)
('P_B_LATEST', 'ben@swym.com', 'Inst_502', 'Class_C', 4, 'Successfully completed rescue dive.', '2025-11-25 10:01:00', '2025-11-25 10:01:00'),

-- Charlie Green: Latest Stage 2
('P_C_LATEST', 'charlie@swym.com', 'Inst_501', 'Class_A', 2, 'Can float independently.', '2025-11-25 10:02:00', '2025-11-25 10:02:00'),

-- Dana White: Latest Stage 3
('P_D_LATEST', 'dana@swym.com', 'Inst_501', 'Class_B', 3, 'Strong swimmer, refining strokes.', '2025-11-25 10:03:00', '2025-11-25 10:03:00'),

-- Eve Black: Latest Stage 1 (Previously reported in test output)
('P_E_LATEST', 'eve@swym.com', 'Inst_502', 'Class_A', 1, 'Comfortable putting face in water.', '2025-11-25 10:04:00', '2025-11-25 10:04:00');