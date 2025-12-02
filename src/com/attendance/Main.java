package com.attendance; 

import com.attendance.model.Student;
import com.attendance.model.AttendanceRecord;
import com.attendance.repository.StudentRepository;
import com.attendance.repository.AttendanceRepository;
import com.attendance.service.SchoolService;

import java.io.IOException;
import java.util.*; 

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    //Initialize All the Layers: UI -> Service -> Repository
    private static final StudentRepository studentRepo = new StudentRepository();
    private static final AttendanceRepository attendanceRepo = new AttendanceRepository();
    private static final SchoolService service = new SchoolService(studentRepo, attendanceRepo);

    public static void main(String[] args){
        while (true){
            System.out.println("\n--- Attendance Management---");
            System.out.println("1. Add student");
            System.out.println("2. List students");
            System.out.println("3. Mark/Update attendance");
            System.out.println("4. View by Date");
            System.out.println("5. View by Student");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> uiAddStudent();
                    case "2" -> uiListStudents();
                    case "3" -> uiMarkAttendance();
                    case "4" -> uiViewByDate();
                    case "5" -> uiViewByStudent();
                    case "6" -> {
                        service.saveData();
                        System.out.println("Data saved. Exiting.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void uiAddStudent(){
        clearScreen.clear();
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        Student s = service.createStudent(name);
        System.out.println("Added Student ID: " + s.getId());
    }

    private static void uiListStudents(){
        clearScreen.clear();
        List<Student> list = service.getAllStudents();
        if (list.isEmpty()) System.out.println("No students.");
        for (Student s : list) {
            System.out.println(s.getId() + " - " + s.getName());
        }
    }

    private static void uiMarkAttendance() {
        clearScreen.clear();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students to mark.");
            return;
        }

        for (Student s : students) {
            AttendanceRecord existing = service.getAttendance(s.getId(), date);
            String prompt = (existing == null) 
                ? "Mark for " + s.getName() + " (P/A/L): " 
                : "Update for " + s.getName() + " (Current: " + existing.getStatus() + ") (P/A/L): ";
            
            System.out.print(prompt);
            String status = scanner.nextLine();
            
            try {
                service.markAttendance(s.getId(), date, status);
                System.out.println("Saved.");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void uiViewByDate() {
        clearScreen.clear();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.println("ID\tName\tStatus");
        for (Student s : service.getAllStudents()) {
            AttendanceRecord r = service.getAttendance(s.getId(), date);
            char status = (r != null) ? r.getStatus() : '-';
            System.out.println(s.getId() + "\t" + s.getName() + "\t" + status);
        }
    }

    private static void uiViewByStudent() {
        clearScreen.clear();
        System.out.print("Enter Student ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Student s = service.getStudent(id);
            if (s == null) {
                System.out.println("Student not found.");
                return;
            }
            System.out.println("Stats for " + s.getName() + ":");
            System.out.println(service.getStudentStats(id));
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }

    public class clearScreen {
        public static void clear() {
            try {
                if (System.getProperty("os.name").contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } else {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
            } catch (IOException | InterruptedException ex) {
                System.err.println("Error clearing screen: " + ex.getMessage());
            }
        }
    }
}