//Business Logic Layer
package com.attendance.service;

import com.attendance.model.Student;
import com.attendance.model.AttendanceRecord;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.AttendanceRepository;
import java.util.List;

public class SchoolService {
    private final StudentRepository studentRepo;
    private final AttendanceRepository attendanceRepo;

    public SchoolService(StudentRepository sRepo, AttendanceRepository aRepo) {
        this.studentRepo = sRepo;
        this.attendanceRepo = aRepo;
    }

    //--Student Logic--//
    public Student createStudent(String name){
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        int newId = studentRepo.findAll().size() + 1;
        Student s = new Student(newId, name);
        studentRepo.save(s);
        return s;
    }

    public List<Student> getAllStudents(){
        return studentRepo.findAll();
    }

    public Student getStudent(int id){
        return studentRepo.findById(id);
    }

    // --- Attendance Logic ---
    public void markAttendance(int studentId, String date, String statusInput) {
        validateDate(date);
        char status = validateStatus(statusInput);

        //Create record and send to repo
        AttendanceRecord record = new AttendanceRecord(studentId, date, status);
        attendanceRepo.add(record);
    }

    public void updateAttendance(int studentId, String date, String statusInput) {
        validateDate(date);
        char status = validateStatus(statusInput);

        AttendanceRecord existing = attendanceRepo.findByStudentAndDate(studentId, date);
        if (existing == null) {
            throw new IllegalArgumentException("No existing record found for student ID " + studentId + " on " + date);
        }

        existing.setStatus(status);
        attendanceRepo.update(existing);
    }

    public AttendanceRecord getAttendance(int studentId, String date) {
        return attendanceRepo.findByStudentAndDate(studentId, date);
    }

    public List<AttendanceRecord> getAllAttendance() {
        return attendanceRepo.findAll();
    }

    // --- Validation Checks---
    private void validateDate(String date) {
        if (date == null || date.length() != 10 || date.charAt(4) != '-' || date.charAt(7) != '-') {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
        } else if (date.isEmpty()) {
            throw new IllegalArgumentException("Date cannot be empty.");
        }
    }

    private char validateStatus(String input) {
        if (input == null) throw new IllegalArgumentException("Status cannot be empty");
        String clean = input.trim().toUpperCase();
        if (!clean.equals("P") && !clean.equals("A") && !clean.equals("L")) {
            throw new IllegalArgumentException("Invalid status. Use P, A, or L.");
        }
        return clean.charAt(0);
    }
    
    // --- Aggregation Logic ---
    public String getStudentStats(int studentId) {
        int present = 0, absent = 0, late = 0;
        boolean found = false;
        
        for (AttendanceRecord r : attendanceRepo.findAll()) {
            if (r.getStudentId() == studentId) {
                found = true;
                switch (r.getStatus()) {
                    case 'P' -> present++;
                    case 'A' -> absent++;
                    case 'L' -> late++;
                }
            }
        }
        
        if (!found) return "No records found.";
        return String.format("Present: %d, Absent: %d, Late: %d", present, absent, late);
    }
    
    public void saveData() {
        studentRepo.saveToFile();
        attendanceRepo.saveToFile();
    }
}
