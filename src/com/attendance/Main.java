//Presentation Layer
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
            System.out.println("3. Mark attendance");
            System.out.println("4. Update attendance");
            System.out.println("5. View by Date");
            System.out.println("6. View by Student");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> listStudents();
                    case "3" -> markAttendance();
                    case "4" -> updateAttendance();
                    case "5" -> viewByDate();
                    case "6" -> viewByStudent();
                    case "7" -> {
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

    private static void addStudent(){
        clearScreen.clear();
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        Student s = service.createStudent(name);
        System.out.println("Added Student ID: " + s.getId());
    }

    private static void listStudents(){
        clearScreen.clear();
        List<Student> list = service.getAllStudents();
        if (list.isEmpty()) System.out.println("No students.");
        for (Student s : list) {
            System.out.println(s.getId() + " - " + s.getName());
        }
    }

    private static void markAttendance(){
        clearScreen.clear();
        if (service.getAllStudents().isEmpty()) {
            System.out.println("No students available to mark attendance.");
            return;
        }

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();

        for (Student s: service.getAllStudents()){
            System.out.print("Mark attendance for " + s.getName() + " (P/A/L): ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.isEmpty() || (!status.equals("P") && !status.equals("A") && !status.equals("L"))) {
                System.out.println("Invalid status. Skipping " + s.getName() + ".");
                continue;
            }
            
            char statChar = status.charAt(0);

            service.markAttendance(s.getId(), date, String.valueOf(statChar));
            if (service.getAttendance(s.getId(), date) != null) {
                System.out.println("Attendance marked for " + s.getName() + ".");
            } else {
                System.out.println("Failed to mark attendance for " + s.getName() + ".");
            }
        }
    }

    private static void updateAttendance(){
        clearScreen.clear();
        if (service.getAllStudents().isEmpty()) {
            System.out.println("No students available to update attendance.");
            return;
        }

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();

        for (Student s: service.getAllStudents()){
            AttendanceRecord existing = service.getAttendance(s.getId(), date);
            if (existing == null) {
                System.out.println("No existing record for " + s.getName() + " on " + date + ". Skipping.");
                continue;
            }

            System.out.print("Update attendance for " + s.getName() + " (Current: " + existing.getStatus() + ") (P/A/L): ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.isEmpty() || (!status.equals("P") && !status.equals("A") && !status.equals("L"))) {
                System.out.println("Invalid status. Skipping " + s.getName() + ".");
                continue;
            }
            
            char statChar = status.charAt(0);

            service.updateAttendance(s.getId(), date, String.valueOf(statChar));
            System.out.println("Attendance updated for " + s.getName() + ".");
        }
    }

    private static void viewByDate() {
        clearScreen.clear();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.println("ID\tName\t\tStatus");
        for (Student s : service.getAllStudents()) {
            AttendanceRecord r = service.getAttendance(s.getId(), date);
            char status = (r != null) ? r.getStatus() : '-';
            System.out.println(s.getId() + "\t" + s.getName() + "\t" + status);
        }
    }

    private static void viewByStudent() {
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