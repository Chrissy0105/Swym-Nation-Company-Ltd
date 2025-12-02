package com.swym.services;

import com.swym.models.ProgressRecord;
import com.swym.persistence.DatabaseAdapter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProgressManager {
    private final DatabaseAdapter db;
    private final AccessControl access;

    public ProgressManager(DatabaseAdapter db, AccessControl access) {
        this.db = db;
        this.access = access;
    }

    public String assignStage(String instructorId, String studentId, int stageValue, String termNotes) {
        if (!access.hasInstructorPrivileges(instructorId))
            return "ERROR: Unauthorized access";

        // Input Validation Fix: Stages must be between 1 and 8
        if (stageValue < 1 || stageValue > 8) {
            return "ERROR: Stage assignment failed. Invalid stage value: " + stageValue;
        }

        String recordId = UUID.randomUUID().toString();
        ProgressRecord record = new ProgressRecord(recordId, studentId, instructorId, "classA", stageValue, termNotes);

        db.insertProgressRecord(record);
        db.updateStudentStage(studentId, stageValue);
        return "SUCCESS: Stage assigned";
    }

    public List<ProgressRecord> getStageHistory(String studentId) {
        return db.queryProgressHistory(studentId);
    }

    public String updateStageEntry(String instructorId, String recordId, int newStage, String newNotes) {
        if (!access.hasInstructorPrivileges(instructorId))
            return "ERROR: Unauthorized access";

        // Add validation for update method as well
        if (newStage < 1 || newStage > 8) {
            return "ERROR: Update failed. Invalid stage value: " + newStage;
        }

        ProgressRecord old = db.getProgressRecord(recordId);
        if (old == null)
            return "ERROR: Record not found";

        old.setStage(newStage);
        old.setNotes(newNotes);
        old.setUpdatedAt(LocalDateTime.now());
        db.updateProgressRecord(old);
        return "SUCCESS: Progress updated";
    }

    public int getCurrentStage(String studentId) {
        return db.getStudentCurrentStage(studentId);
    }
}