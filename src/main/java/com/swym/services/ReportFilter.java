package com.swym.services;

import java.time.LocalDate;

public class ReportFilter {
    private LocalDate start;
    private LocalDate end;
    private String studentId;
    private String classId;
    private String instructorId;

    public ReportFilter() {}
    public ReportFilter(LocalDate start, LocalDate end, String studentId, String classId, String instructorId) {
        this.start = start; this.end = end; this.studentId = studentId;
        this.classId = classId; this.instructorId = instructorId;
    }

    // Getters and setters
    public LocalDate getStart() { return start; }
    public void setStart(LocalDate start) { this.start = start; }
    public LocalDate getEnd() { return end; }
    public void setEnd(LocalDate end) { this.end = end; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
}
