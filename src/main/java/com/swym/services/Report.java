package com.swym.services;

import java.util.List;
import java.util.Map;

public class Report {
    private String title;
    private List<Map<String, Object>> rows;

    public Report(String title, List<Map<String, Object>> rows) {
        this.title = title; this.rows = rows;
    }

    public String getTitle() { return title; }
    public List<Map<String, Object>> getRows() { return rows; }
}
