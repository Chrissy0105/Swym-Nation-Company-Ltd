***

# Attendance Management System (Layered Architecture)

A robust console-based Java application designed to track student attendance. This project demonstrates the transition from a Monolithic architecture to a structured **Layered Architecture**, ensuring separation of concerns, maintainability, and scalability.

## 🚀 Features

*   **Student Management:** Register new students with auto-incremented IDs.
*   **Attendance Tracking:** Mark attendance as Present (P), Absent (A), or Late (L).
*   **Data Persistence:** Automatically saves and loads data using CSV files (`students.csv`, `attendance.csv`).
*   **Reporting:**
    *   View attendance sheets by specific date.
    *   View individual student statistics (Total Present/Absent/Late).
*   **Validation:** Prevents invalid dates and status codes via the Business Logic layer.

## 🏗️ Architectural Design

This project follows the **Layered Architecture** pattern, dividing the code into four distinct logical layers:

1.  **Presentation Layer (`com.attendance.Main`)**
    *   Handles user input and prints output to the console.
    *   Communicates only with the Service Layer.
2.  **Service Layer (`com.attendance.service`)**
    *   Contains business logic (e.g., calculating stats, validating date formats).
    *   Acts as a bridge between the UI and the Data layer.
3.  **Data Access Layer / Repository (`com.attendance.repository`)**
    *   Handles file I/O operations.
    *   Reads and writes to CSV files.
4.  **Model Layer (`com.attendance.model`)**
    *   Simple POJOs (Plain Old Java Objects) representing `Student` and `AttendanceRecord`.

## 📂 Project Structure

```text
AttendanceApp/
│
├── data/                        # CSV storage folder
│   ├── students.csv             # Created automatically
│   └── attendance.csv           # Created automatically
│
├── src/
│   └── com/
│       └── attendance/
│           ├── Main.java        # Entry Point (UI)
│           ├── model/           # Data Classes
│           ├── repository/      # File Handling
│           └── service/         # Business Logic
│
└── README.md                    # Project Documentation
```

## 🛠️ Prerequisites

*   **Java Development Kit (JDK):** Version 8 or higher.

## ⚡ How to Run

### 1. Compile
Open your terminal/command prompt, navigate to the `src` directory, and run:

```bash
cd AttendanceApp/src
javac com/attendance/Main.java
```

### 2. Run
Execute the program from the `src` directory using the fully qualified class name:

```bash
java com.attendance.Main
```

*Note: Ensure the `data/` folder exists in the root `AttendanceApp/` directory. If the program cannot find the CSV files, it will create new ones, but the folder must exist.*

## 💾 Data Storage Format

The application stores data in `CSV` format in the `data/` directory.

**students.csv**
```csv
1,John Doe
2,Jane Smith
```

**attendance.csv**
```csv
1,2023-10-25,P
2,2023-10-25,A
```
