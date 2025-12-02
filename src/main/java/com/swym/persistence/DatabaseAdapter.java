package com.swym.persistence;

import java.util.List;
import java.util.Map;
import com.swym.models.ProgressRecord;
import com.swym.models.AdultRegistrant;
import com.swym.models.ChildRegistrant;

public interface DatabaseAdapter {
    // Progress methods
    void insertProgressRecord(ProgressRecord r);
    ProgressRecord getProgressRecord(String recordId);
    List<ProgressRecord> queryProgressHistory(String studentId);
    void updateProgressRecord(ProgressRecord r);
    void updateStudentStage(String studentId, int stage);
    int getStudentCurrentStage(String studentId);
    List<Map<String, Object>> runReportQuery(String sql, Object... params);

    // Registration methods
    void insertAdult(AdultRegistrant adult);
    AdultRegistrant getAdult(String email);
    List<AdultRegistrant> getAllAdults();

    void insertChild(ChildRegistrant child);
    ChildRegistrant getChild(String name);
    List<ChildRegistrant> getAllChildren();
}
