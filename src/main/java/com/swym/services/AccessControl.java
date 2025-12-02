package com.swym.services;

public interface AccessControl {
    boolean hasInstructorPrivileges(String userId);

    boolean hasReportAccess(String userId);
}
