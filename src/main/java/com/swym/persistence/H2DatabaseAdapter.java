package com.swym.persistence;

import com.swym.models.AdultRegistrant;
import com.swym.models.ChildRegistrant;
import com.swym.models.ProgressRecord;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

public class H2DatabaseAdapter implements DatabaseAdapter {

    private static final String JDBC_URL = "jdbc:h2:./swymdb;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    public H2DatabaseAdapter() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Adults table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS adults (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "dob DATE NOT NULL," +
                    "contactNumber VARCHAR(20) NOT NULL," +
                    "emergencyContact VARCHAR(100)," +
                    "email VARCHAR(100) NOT NULL UNIQUE," +
                    "medicalInfo TEXT," +
                    "goals TEXT)");

            // Children table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS children (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "childName VARCHAR(100) NOT NULL," +
                    "parentName VARCHAR(100) NOT NULL," +
                    "age INT NOT NULL," +
                    "behaviorNotes TEXT," +
                    "illnesses TEXT," +
                    "email VARCHAR(100) NOT NULL UNIQUE)");

            // Progress records table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS progress_records (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "studentId VARCHAR(100)," +
                    "instructorId VARCHAR(100)," +
                    "classId VARCHAR(100)," +
                    "stage INT," +
                    "notes TEXT," +
                    "createdAt TIMESTAMP," +
                    "updatedAt TIMESTAMP)");

            // Operational view for reports
            stmt.executeUpdate(
                    "CREATE OR REPLACE VIEW operational_view AS " +
                            "SELECT p.id AS record_id, " +
                            "       p.studentId AS student_id, " +
                            "       COALESCE(a.name, c.childName, p.studentId) AS student_name, " +
                            "       p.classId AS class_id, " +
                            "       p.instructorId AS instructor_id, " +
                            "       p.stage AS stage, " +
                            "       p.notes AS notes, " +
                            "       p.createdAt AS date " +
                            "FROM progress_records p " +
                            "LEFT JOIN adults a ON p.studentId = a.email " +
                            "LEFT JOIN children c ON p.studentId = c.email");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASS);
    }

    // Adult methods
    @Override
    public void insertAdult(AdultRegistrant adult) {
        String sql = "INSERT INTO adults (name, dob, contactNumber, emergencyContact, email, medicalInfo, goals) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, adult.getName());
            ps.setDate(2, java.sql.Date.valueOf(adult.getDob()));
            ps.setString(3, adult.getContactNumber());
            ps.setString(4, adult.getEmergencyContact());
            ps.setString(5, adult.getEmail());
            ps.setString(6, adult.getMedicalInfo());
            ps.setString(7, String.join(",", adult.getGoals()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AdultRegistrant getAdult(String email) {
        String sql = "SELECT * FROM adults WHERE email=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new AdultRegistrant(
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("contactNumber"),
                        rs.getString("emergencyContact"),
                        rs.getString("email"),
                        rs.getString("medicalInfo"),
                        Arrays.asList(rs.getString("goals").split(",")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AdultRegistrant> getAllAdults() {
        List<AdultRegistrant> list = new ArrayList<>();
        String sql = "SELECT * FROM adults";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AdultRegistrant(
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("contactNumber"),
                        rs.getString("emergencyContact"),
                        rs.getString("email"),
                        rs.getString("medicalInfo"),
                        Arrays.asList(rs.getString("goals").split(","))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Child methods
    @Override
    public void insertChild(ChildRegistrant child) {
        String sql = "INSERT INTO children (childName, parentName, age, behaviorNotes, illnesses, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, child.getName());
            ps.setString(2, child.getParentName());
            ps.setInt(3, child.getAge());
            ps.setString(4, child.getBehaviorNotes());
            ps.setString(5, child.getIllnesses());
            ps.setString(6, child.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChildRegistrant getChild(String childId) {
        String sql = "SELECT * FROM children WHERE id=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, childId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ChildRegistrant(
                        rs.getString("childName"),
                        rs.getString("parentName"),
                        rs.getInt("age"),
                        rs.getString("behaviorNotes"),
                        rs.getString("illnesses"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ChildRegistrant> getAllChildren() {
        List<ChildRegistrant> list = new ArrayList<>();
        String sql = "SELECT * FROM children";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ChildRegistrant(
                        rs.getString("childName"),
                        rs.getString("parentName"),
                        rs.getInt("age"),
                        rs.getString("behaviorNotes"),
                        rs.getString("illnesses"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Progress methods
    @Override
    public void insertProgressRecord(ProgressRecord r) {
        String sql = "INSERT INTO progress_records (id, studentId, instructorId, classId, stage, notes, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getId());
            ps.setString(2, r.getStudentId());
            ps.setString(3, r.getInstructorId());
            ps.setString(4, r.getClassId());
            ps.setInt(5, r.getStage());
            ps.setString(6, r.getNotes());
            ps.setTimestamp(7, Timestamp.valueOf(r.getCreatedAt()));
            ps.setTimestamp(8, Timestamp.valueOf(r.getUpdatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProgressRecord getProgressRecord(String recordId) {
        String sql = "SELECT * FROM progress_records WHERE id=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recordId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapProgressRecord(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProgressRecord> queryProgressHistory(String studentId) {
        List<ProgressRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM progress_records WHERE studentId=? ORDER BY createdAt";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProgressRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updateProgressRecord(ProgressRecord r) {
        String sql = "UPDATE progress_records SET stage=?, notes=?, updatedAt=? WHERE id=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getStage());
            ps.setString(2, r.getNotes());
            ps.setTimestamp(3, Timestamp.valueOf(r.getUpdatedAt()));
            ps.setString(4, r.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStudentStage(String studentId, int newStage) {
        String sql = "UPDATE progress_records SET stage=?, updatedAt=? WHERE studentId=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStage);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, studentId);
            int updated = ps.executeUpdate();

            // If student has no record yet → insert
            if (updated == 0) {
                String insertSql = "INSERT INTO progress_records (id, studentId, stage, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps2 = conn.prepareStatement(insertSql)) {
                    ps2.setString(1, UUID.randomUUID().toString());
                    ps2.setString(2, studentId);
                    ps2.setInt(3, newStage);
                    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                    ps2.setTimestamp(4, now);
                    ps2.setTimestamp(5, now);
                    ps2.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getStudentCurrentStage(String studentId) {
        String sql = "SELECT stage FROM progress_records WHERE studentId=? ORDER BY updatedAt DESC LIMIT 1";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt("stage");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> runReportQuery(String sql, Object... params) {
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // Helper functiom
    private ProgressRecord mapProgressRecord(ResultSet rs) throws SQLException {
        return new ProgressRecord(
                rs.getString("id"),
                rs.getString("studentId"),
                rs.getString("instructorId"),
                rs.getString("classId"),
                rs.getInt("stage"),
                rs.getString("notes"),
                rs.getTimestamp("createdAt").toLocalDateTime(),
                rs.getTimestamp("updatedAt").toLocalDateTime());
    }
}
