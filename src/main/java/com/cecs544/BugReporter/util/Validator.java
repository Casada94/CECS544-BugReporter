package com.cecs544.BugReporter.util;

import com.cecs544.BugReporter.enums.Role;
import com.cecs544.BugReporter.model.Account;
import com.cecs544.BugReporter.model.BugData;
import com.cecs544.BugReporter.model.Program;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public static String validAccount(Account account) {
        StringBuilder errorMessage = new StringBuilder();

        if (account.getUSERNAME() == null || account.getUSERNAME().isBlank()) {
            errorMessage.append("USERNAME is required. ");
        }
        if (account.getFIRST_NAME() == null || account.getFIRST_NAME().isBlank()) {
            errorMessage.append("First Name is required. ");
        }
        if (account.getLAST_NAME() == null || account.getLAST_NAME().isBlank()) {
            errorMessage.append("Last Name is required. ");
        }
        if (account.getAUTHORITY() == null || account.getAUTHORITY().isBlank()) {
            errorMessage.append("AUTHORITY is required. ");
        }

        return errorMessage.toString();
    }

    public static String validProgram(Program program) {
        StringBuilder errorMessage = new StringBuilder();

        if (program.getNAME() == null || program.getNAME().isEmpty()) {
            errorMessage.append("Program Name is required. ");
        }
        Map<String, Map<String, List<String>>> releaseVersionFunctionMap = program.getReleaseVersionFunctionMap();
        for(String key: releaseVersionFunctionMap.keySet()) {
            Map<String, List<String>> versionFunctionMap = releaseVersionFunctionMap.get(key);
            if(versionFunctionMap.isEmpty()){
                errorMessage.append("Version(s) for " + key + " are required. ");
            }else{
                for(String version : versionFunctionMap.keySet()) {
                    List<String> functions = versionFunctionMap.get(version);
                    if(functions.isEmpty()) {
                        errorMessage.append("Function(s) are required. ");
                    }
                }
            }

        }

        return errorMessage.toString();
    }
}
