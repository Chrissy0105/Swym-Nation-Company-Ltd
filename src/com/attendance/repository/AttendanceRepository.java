package com.attendance.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.attendance.model.AttendanceRecord;

public class AttendanceRepository {
    private static final String ATTENDANCE_FILE = "attendance.csv"; 
    private final List<AttendanceRecord> records = new ArrayList<>();

    public AttendanceRepository(){
        load();
    }

    public List<AttendanceRecord> findAll(){
        return records;
    }

    public AttendanceRecord findByStudentAndDate(int studentId, String date){
        return records.stream()
                .filter(r -> r.getStudentId() == studentId && r.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    public void addOrUpdate(AttendanceRecord record){
        // Check if record exists or else add 
        AttendanceRecord existing = findByStudentAndDate(record.getStudentId(), record.getDate());
        if(existing != null){
            existing.setStatus(record.getStatus());
        } else {
            records.add(record);
        }
        saveToFile();
    }

    private void load(){
        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    records.add(new AttendanceRecord(
                        Integer.parseInt(parts[0].trim()), 
                        parts[1].trim(), 
                        parts[2].trim().charAt(0)
                    ));
                }
            }
        } catch (IOException e) { 
            // Log error or ignore if file doesn't exist yet
        }
    }

    public void saveToFile(){
         try (PrintWriter pw = new PrintWriter(new FileWriter(ATTENDANCE_FILE))) {
            for (AttendanceRecord r : records) {
                pw.println(r.getStudentId() + "," + r.getDate() + "," + r.getStatus());
            }
        } catch (IOException e) {
            System.err.println("Error saving attendance: " + e.getMessage());
        }
    }
}
