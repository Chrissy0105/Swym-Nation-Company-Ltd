package com.swym.services;

import com.swym.models.AdultRegistrant;
import com.swym.models.ChildRegistrant;
import com.swym.persistence.H2DatabaseAdapter;
import com.swym.persistence.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class RegistrationService {
    private final H2DatabaseAdapter db;

    public RegistrationService(H2DatabaseAdapter db) {
        this.db = db;
    }

    public void registerAdult(AdultRegistrant adult) {
        db.insertAdult(adult);
    }

    public void registerChild(ChildRegistrant child) {
        db.insertChild(child);
    }

    public List<AdultRegistrant> getAllAdults() {
        List<AdultRegistrant> adults = new ArrayList<>();
        String sql = "SELECT * FROM adults";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AdultRegistrant a = new AdultRegistrant(
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("contactNumber"),
                        rs.getString("emergencyContact"),
                        rs.getString("email"),
                        rs.getString("medicalInfo"),
                        List.of(rs.getString("goals").split(",")) // split CSV goals
                );
                adults.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adults;
    }

    public List<ChildRegistrant> getAllChildren() {
        List<ChildRegistrant> children = new ArrayList<>();
        String sql = "SELECT * FROM children";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ChildRegistrant c = new ChildRegistrant(
                        rs.getString("childName"),
                        rs.getString("parentName"),
                        rs.getInt("age"),
                        rs.getString("behaviorNotes"),
                        rs.getString("illnesses"),
                        rs.getString("email")
                );
                children.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return children;
    }
}
