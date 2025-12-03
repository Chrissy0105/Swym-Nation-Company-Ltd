//Entity
package com.attendance.model;

public class AttendanceRecord {
    private final int studentId;
    private final String date; 
    private char status; // P - Present, A - Absent, L - Late

    public AttendanceRecord(int studentId, String date, char status){
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    public int getStudentId() {
        return studentId;
    }
    public String getDate() {
        return date;
    }
    public char getStatus() {
        return status;
    }
    public void setStatus(char status) {
        this.status = status;
    }
}
