package com.swym.models;

import java.time.LocalDate;
import java.util.List;

public class AdultRegistrant extends Registrant {
    private LocalDate dob;
    private String emergencyContact;
    private String medicalInfo;
    private List<String> goals;

    public AdultRegistrant(String name, LocalDate dob, String contactNumber, String emergencyContact, String email, String medicalInfo, List<String> goals) {
        super(name, contactNumber, email);
        this.dob = dob;
        this.emergencyContact = emergencyContact;
        this.medicalInfo = medicalInfo;
        this.goals = goals;
    }

    public LocalDate getDob()
     { return dob; }

    public String getEmergencyContact()
     { return emergencyContact; }

    public String getMedicalInfo() 
    { return medicalInfo; }
    
    public List<String> getGoals() 
    { return goals; }
}
