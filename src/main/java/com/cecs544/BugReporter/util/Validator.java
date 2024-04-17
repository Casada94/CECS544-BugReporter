package com.cecs544.BugReporter.util;

import com.cecs544.BugReporter.enums.Role;
import com.cecs544.BugReporter.model.BugData;

import java.time.LocalDate;

public class Validator {

    public static String validateInitialSubmission(BugData bugData) {
        StringBuilder errorMessage = new StringBuilder();

        if (bugData.getProgramName().isEmpty()) {
            errorMessage.append("Program Name is required. ");
        }
        if (bugData.getRelease().isEmpty()) {
            errorMessage.append("Release is required. ");
        }
        if (bugData.getVersion().isEmpty()) {
            errorMessage.append("Version is required. ");
        }
        if (bugData.getReportType() == null) {
            errorMessage.append("Report Type is required. ");
        }
        if (bugData.getSeverity() == null) {
            errorMessage.append("Severity is required. ");
        }
        if (bugData.getProblemSummary().isEmpty()) {
            errorMessage.append("Problem Summary is required. ");
        }
        if (bugData.getProblemDescription().isEmpty()) {
            errorMessage.append("Problem Description is required. ");
        }
        if (bugData.getReportedBy().isEmpty()) {
            errorMessage.append("Reported By is required. ");
        }
        if (bugData.getReportedDate() == null) {
            errorMessage.append("Reported Date is required. ");
        }

        return errorMessage.isEmpty() ? null : errorMessage.toString();
    }

    public static Role determineUserType(String authority) {
        switch (authority) {
            case Constants.ADMIN:
                return Role.ADMIN;
            case Constants.USER:
                return Role.USER;
            case Constants.EMPLOYEE:
                return Role.EMPLOYEE;
            case Constants.MANAGER:
                return Role.MANAGER;
            default:
                throw new IllegalStateException("Unexpected value: " + authority);
        }
    }

    public static java.sql.Date nullOrDate(LocalDate date) {
        return date == null ? null : java.sql.Date.valueOf(date);
    }

    public static String emptyIfNull(String str) {
        return str == null ? Constants.EMPTY : str;
    }

    public static String nullOrString(String input) {
        return input == null ? null : input.isEmpty() ? null : input;
    }
}
