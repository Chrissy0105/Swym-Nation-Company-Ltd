package com.swym.services;

import com.swym.persistence.DatabaseAdapter;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    private final DatabaseAdapter db;
    private final AccessControl access;

    public ReportGenerator(DatabaseAdapter db, AccessControl access) {
        this.db = db;
        this.access = access;
    }

    public Report generateReport(String userId, ReportFilter filter) {
        if (!access.hasReportAccess(userId)) throw new SecurityException("Unauthorized");
        String sql = buildQuery(filter);
        List<Map<String,Object>> dataset = db.runReportQuery(sql);
        return new Report("Custom Report", dataset);
    }

    private String buildQuery(ReportFilter f) {
        StringBuilder q = new StringBuilder("SELECT * FROM operational_view WHERE TRUE");
        if (f.getStart()!=null && f.getEnd()!=null)
            q.append(" AND date BETWEEN '").append(f.getStart()).append("' AND '").append(f.getEnd()).append("'");
        if (f.getStudentId()!=null) q.append(" AND student_id='").append(f.getStudentId()).append("'");
        if (f.getClassId()!=null) q.append(" AND class_id='").append(f.getClassId()).append("'");
        if (f.getInstructorId()!=null) q.append(" AND instructor_id='").append(f.getInstructorId()).append("'");
        return q.toString();
    }

    public String exportCSV(Report report, String outputPath) { return "CSV Export Successful"; }
    public String exportPDF(Report report, String outputPath) { return "PDF Export Successful"; }
}
