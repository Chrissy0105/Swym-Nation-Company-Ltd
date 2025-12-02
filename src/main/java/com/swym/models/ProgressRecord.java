package com.swym.models;

import java.time.LocalDateTime;

public class ProgressRecord {
    private String id;
    private String studentId;
    private String instructorId;
    private String classId;
    private int stage; // 1..8
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor with timestamps
    public ProgressRecord(String id, String studentId, String instructorId, String classId, int stage, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.classId = classId;
        this.stage = stage;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor without timestamps (creates current timestamps automatically)
    public ProgressRecord(String id, String studentId, String instructorId, String classId, int stage, String notes) {
        this(id, studentId, instructorId, classId, stage, notes, LocalDateTime.now(), LocalDateTime.now());
    }

    // Getters and setters
    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getInstructorId() { return instructorId; }
    public String getClassId() { return classId; }
    public int getStage() { return stage; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setStage(int stage) { this.stage = stage; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

