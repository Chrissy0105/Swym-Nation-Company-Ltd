import java.io.*; 
import java.util.*; 

class Student {
    int id; 
    String name; 

    Student(int id, String name) {
        this.id = id; 
        this.name = name; 
    }
}

class attendanceRecord {
    int studentId;
    String date; 
    char status;  //P = Present, A = Absent, L = Late

    attendanceRecord(int studentId, String date, char status){
        this.studentId = studentId; 
        this.date = date; 
        this.status = status; 
    }
}

public class attendanceManager{
    private static final String STUDENTS_FILE = "students.csv";
    private static final String ATTENDANCE_FILE = "attendance.csv";
    private static final Scanner scanner = new Scanner(System.in); 

    private static final List<Student> students = new ArrayList<>();
    private static final List<attendanceRecord> attendanceRecords = new ArrayList<>(); 

    // ---------- CORE FEATURES ----------
    private static void addStudent(){
        clearScreen.clear();
        System.out.print("\nEnter student name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()){
            System.err.println("Name cannot be empty.");
            return;
        }

        int id = students.size() + 1;  // Simple ID assignment protocol 
        students.add(new Student(id, name));
        System.out.println("Added student: " + id + " - " + name);
    }

    private static void listStudents(){
        clearScreen.clear();
        if (students.isEmpty()){
            System.out.println("No students available.");
            return; 
        }

        System.out.println("\nStudent List:");
        for (Student s : students){
            System.out.println("ID: " + s.id + ", Name: " + s.name);
        }
    }

    private static void markAttendance(){
        clearScreen.clear();
        if (students.isEmpty()){
            System.out.println("No students available to mark attendance.");
            return; 
        }

        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        if (date.isEmpty()){
            System.err.println("Date cannot be empty.");
            return; 
        } else if (date.length() != 10 || date.charAt(4) != '-' || date.charAt(7) != '-') {
            System.err.println("Date format is incorrect. Use YYYY-MM-DD.");
            return;
        }

        for (Student s: students){
            System.out.print("Mark attendance for " + s.name + " (P/A/L): ");
            String statusInput = scanner.nextLine().trim().toUpperCase();
            if (statusInput.isEmpty() || !(statusInput.equals("P") || statusInput.equals("A") || statusInput.equals("L"))){
                System.err.println("Invalid status. Use P, A, or L.");
                continue; 
            }
            char status = statusInput.charAt(0);

            attendanceRecord existingRecord = findAttendace(s.id, date);
            if (existingRecord != null){
                existingRecord.status = status; 
                System.out.println("Updated attendance for " + s.name);
            } else {
                attendanceRecords.add(new attendanceRecord(s.id, date, status));
                System.out.println("Marked attendance for " + s.name);
            }
        }

        System.out.println("Attendance marking completed for date: " + date);

    }
    private static void updateAttendance(){
        clearScreen.clear();
        System.out.print("Enter date (YYYY-MM-DD) to update attendance: ");
        String date = scanner.nextLine().trim();
        if (date.isEmpty()){
            System.err.println("Date cannot be empty.");
            return; 
        } else if (date.length() != 10 || date.charAt(4) != '-' || date.charAt(7) != '-') {
            System.err.println("Date format is incorrect. Use YYYY-MM-DD.");
            return;
        }

        for (Student s: students){
            attendanceRecord record = findAttendace(s.id, date);
            if (record == null){
                System.out.println("No existing record for " + s.name + " on " + date + ". Skipping.");
                continue; 
            }

            System.out.print("Current status for " + s.name + " is " + record.status + ". Enter new status (P/A/L): ");
            String statusInput = scanner.nextLine().trim().toUpperCase();
            if (statusInput.isEmpty() || !(statusInput.equals("P") || statusInput.equals("A") || statusInput.equals("L"))){
                System.err.println("Invalid status. Use P, A, or L.");
                continue; 
            }
            char status = statusInput.charAt(0);
            record.status = status; 
            System.out.println("Updated attendance for " + s.name);
        }

        System.out.println("Attendance update completed for date: " + date);
    }

    private static attendanceRecord findAttendace(int studentId, String date){
        for (attendanceRecord r : attendanceRecords){
            if (r.studentId == studentId && r.date.equals((date))){
                return r; 
            }
        }
        return null; 
        
    }

    private static Student findStudent(int id){
        for (Student s: students){
            if (s.id == id){
                return s; 
            }
        }
        return null; 
    }

    private static void viewByStudent(){
        clearScreen.clear();
        System.out.println("Enter student ID:");
        int id ; 
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format.");
            return;
        }

        Student student = findStudent(id);
        if (student == null){
            System.err.println("Student not found.");
            return; 
        }

        System.out.println("Attendance for " + student.name + ":");
        int total = 0, present = 0, absent = 0, late = 0;
        for (attendanceRecord r : attendanceRecords){
            if (r.studentId == id){
                System.out.println(r.date + "\t" + r.status);
                total++;
                switch (r.status) {
                    case 'P' -> present++;
                    case 'A' -> absent++;
                    case 'L' -> late++;
                }
            }
        } 
        if (total == 0){
            System.out.println("No attendance records found for this student.");
            return; 
        }
        System.out.println("Total Attendance Records: " + total);
        System.out.println("Present: " + present);
        System.out.println("Absent: " + absent);
        System.out.println("Late: " + late);
    }

    private static void viewByDate(){
        clearScreen.clear();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        if (date.isEmpty()){
            System.err.println("Date cannot be empty.");
            return; 
        } else if (date.length() != 10 || date.charAt(4) != '-' || date.charAt(7) != '-') {
            System.err.println("Date format is incorrect. Use YYYY-MM-DD.");
            return;
        }

        System.out.println("Attendance for date: " + date + ":");
        System.out.println("ID\tName\t\tStatus");

        for(Student s : students){
            attendanceRecord record = findAttendace(s.id, date);
            char status = (record != null) ? record.status : 'N'; // N = Not marked
            System.out.println(s.id + "\t" + s.name + "\t" + status);
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

    // ---------- FILE I/O ----------
    private static void loadStudents(){
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE))){
            String line; 
            while ((line = br.readLine()) != null){
                String[] parts = line.split(",",2);
                if (parts.length == 2){
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    students.add(new Student(id, name));
                }
            }
        } catch (IOException e){
            System.err.println("Error loading students: " + e.getMessage());
        }

    }

    private static void saveStudents(){
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENTS_FILE))){
            for (Student s : students){
                pw.println(s.id + "," + s.name);
            }
        } catch (IOException e){
            System.err.println("Error saving students: " + e.getMessage());
        }

    }
    
    private static void loadAttendance(){
        try(BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_FILE))){
            String line; 
            while ((line = br.readLine()) != null){
                String[] parts = line.split(",",3);
                if (parts.length == 3){
                    int studentId = Integer.parseInt(parts[0].trim());
                    String date = parts[1].trim();
                    char status = parts[2].trim().charAt(0);
                    attendanceRecords.add(new attendanceRecord(studentId, date, status));
                }
            }
        } catch (IOException e){
            System.err.println("Error loading attendance: " + e.getMessage());
        }

    }

    private static void saveAttendance(){
        try(PrintWriter pw = new PrintWriter(new FileWriter(ATTENDANCE_FILE))){
            for (attendanceRecord r : attendanceRecords){
                pw.println(r.studentId + "," + r.date + "," + r.status);
            }
        } catch (IOException e){
            System.err.println("Error saving attendance: " + e.getMessage());
        }

    }

    // ---------- MAIN MENU ----------
    public static void main(String[] args){
        loadStudents(); 
        loadAttendance(); 

        while(true){
            System.out.println("\n--- Attendance Management ---");
            System.out.println("1. Add student");
            System.out.println("2. List students");
            System.out.println("3. Mark attendance");
            System.out.println("4. Update attendance");
            System.out.println("5. View attendance by date");
            System.out.println("6. View attendance by student");
            System.out.println("7. Save data");
            System.out.println("8. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim(); 
            switch (choice) {
                case "1" -> addStudent();
                case "2" -> listStudents();
                case "3" -> markAttendance();
                case "4" -> updateAttendance();
                case "5" -> viewByDate();
                case "6" -> viewByStudent();
                case "7" -> {
                    saveStudents();
                    saveAttendance();
                    System.out.println("Data saved.");
                }
                case "8" -> {
                    saveStudents();
                    saveAttendance();
                    System.out.println("Exiting. Data saved.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

}
