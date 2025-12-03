//Data Acess Layer
package com.attendance.repository;

import com.attendance.model.Student;
import java.io.*;
import java.util.*; 


public class StudentRepository {
    public static final String STUDENT_FILE = "C:\\Users\\dejor\\Downloads\\Projects\\Software Engineering Coding\\Swym-Nation-Company-Ltd\\data\\students.csv";
    private final List<Student> students = new ArrayList<>();

    public StudentRepository() {
        load();
    }

    public List<Student> findAll() {
        return students;
    }

    public Student findById(int id){
        return students.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public void save(Student student){
        students.add(student);
        saveToFile();
    }

    private void load(){
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    students.add(new Student(Integer.parseInt(parts[0].trim()), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            // Log error or ignore if file doesn't exist yet
        }
    }

    public void saveToFile(){
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (Student s : students) {
                pw.println(s.getId() + "," + s.getName());
            }
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }
}
