package com.cecs544.BugReporter.util;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String USER_QUERY = "SELECT USERNAME,PASSWORD,ENABLED FROM ACCOUNTS WHERE USERNAME = ?";
    public static final String AUTHORITY_QUERY = "SELECT USERNAME, AUTHORITY FROM ACCOUNTS WHERE USERNAME=?";

    public static final String PROGRAM = "PROGRAM";
    public static final String RELEASE = "RELEASE";
    public static final String VERSION = "VERSION";
    public static final String PROBLEM_NUMBER = "PROB.#";
    public static final String REPORT_TYPE = "REPORT TYPE";
    public static final String SEVERITY = "SEVERITY";
    public static final String IF_YES_DESCRIBE = "if yes describe";
    public static final String ATTACHMENTS = "ATTACHMENTS";
    public static final String PROBLEM_SUMMARY = "PROBLEM SUMMARY";
    public static final String REPRODUCIBLE = "REPRODUCIBLE";
    public static final String PROBLEM_DESC_AND_REPRODUCE = "PROBLEM AND HOW TO REPRODUCE IT";
    public static final String SUGGESTED_FIX = "SUGGESTED FIX";
    public static final String REPORTED_BY = "REPORTED BY";
    public static final String REPORTED_DATE = "REPORTED DATE";
    public static final String FUNCTIONAL_AREA = "FUNCTIONAL AREA";
    public static final String ASSIGNED_TO = "ASSIGNED TO";
    public static final String COMMENTS = "COMMENTS";
    public static final String STATUS = "STATUS";
    public static final String PRIORITY = "PRIORITY";
    public static final String RESOLUTION = "RESOLUTION";
    public static final String RESOLUTION_RELEASE = "RESOLUTION RELEASE";
    public static final String RESOLUTION_VERSION = "RESOLUTION VERSION";
    public static final String FOR_OFFICE_USE_ONLY = "ITEMS BELOW ARE FOR USE ONLY BY THE DEVELOPMENT TEAM";
    public static final String RESOLVED_BY = "RESOLVED BY";
    public static final String RESOLVED_DATE = "RESOLVED DATE";
    public static final String TESTED_BY = "TESTED BY";
    public static final String TESTED_DATE = "TESTED DATE";
    public static final String TREAT_AS_DEFERRED = "TREAT AS DEFERRED";

    public static final String INITIAL = "intial";
    public static final String USER_REVIEW = "user_review";
    public static final String EMPLOYEE_REVIEW = "employee_review";
    public static final String MANAGER_REVIEW = "manager_review";
    public static final String ADMIN_REVIEW = "admin_review";

    public static final String ADMIN = "ADMIN";
    public static final String MANAGER = "MANAGER";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String USER = "USER";

    public static final String CODING_ERROR = "Coding Error";
    public static final String DESIGN_ERROR = "Design Error";
    public static final String SUGGESTION = "Suggestion";
    public static final String DOCUMENTATION = "Documentation";
    public static final String HARDWARE = "Hardware";
    public static final String QUERY = "Query";

    public static final String PENDING = "Pending";
    public static final String FIXED = "Fixed";
    public static final String IRREPRODUCIBLE = "Irreproducible";
    public static final String DEFERRED = "Deferred";
    public static final String AS_DESIGNED = "As Designed";
    public static final String CANNOTFIX = "Cannot Fix";
    public static final String WITHDRAWN_BY_REPORTER = "Withdrawn by Reporter";
    public static final String NEED_MORE_INFO = "Need More Info";
    public static final String DISAGREE_WITH_SUGGESTION = "Disagree with Suggestion";

    public static final String OPEN = "OPEN";
    public static final String EMPTY = "";
    public static final String CLOSED = "CLOSED";

    public static final String FATAL = "Fatal";
    public static final String SERIOUS = "Serious";
    public static final String MINOR = "Minor";

    public static final String COMPANY_NAME = "Bring Your Own Bug";
    public static final String CONFIDENTIAL = "CONFIDENTIAL";

    public static final String SUBMIT = "SUBMIT";
    public static final String UPDATE = "UPDATE";

    public static final String NAME_FIELD_WIDTH = "300px";
    public static final String TEXT_AREA_HEIGHT = "200px";

    public static final List<String> reportTypes = new ArrayList<>(List.of(new String[]{CODING_ERROR, DESIGN_ERROR, SUGGESTION, DOCUMENTATION, HARDWARE, QUERY}));
    public static final List<String> severities = new ArrayList<>(List.of(new String[]{FATAL, SERIOUS, MINOR}));
    public static final List<Integer> priorities = new ArrayList<>(List.of(new Integer[]{1, 2, 3, 4, 5}));
    public static final List<String> statuses = new ArrayList<>(List.of(new String[]{OPEN, CLOSED}));


    public static final String QUERY_BUG_REPORT_ID = "bugReportId";
    public static final String QUERY_PROGRAM_ID = "programId";
    public static final String QUERY_REPORT_TYPE = "reportType";
    public static final String QUERY_SEVERITY = "severity";
    public static final String QUERY_ATTACHMENTS = "attachments";
    public static final String QUERY_ATTACHMENT_DESC = "attachmentDesc";
    public static final String QUERY_PROBLEM_SUMMARY = "problemSummary";
    public static final String QUERY_REPRODUCIBLE = "reproducible";
    public static final String QUERY_PROBLEM_DESCRIPTION = "problemDescription";
    public static final String QUERY_SUGGESTED_FIX = "suggestedFix";
    public static final String QUERY_REPORTED_BY = "reportedBy";
    public static final String QUERY_REPORTED_DATE = "reportedDate";
    public static final String QUERY_FUNCTIONAL_AREA = "functionalArea";
    public static final String QUERY_ASSIGNED_TO = "assignedTo";
    public static final String QUERY_COMMENTS = "comments";
    public static final String QUERY_STATUS = "status";
    public static final String QUERY_PRIORITY = "priority";
    public static final String QUERY_RESOLUTION = "resolution";
    public static final String QUERY_RESOLUTION_ID = "resolutionId";
    public static final String QUERY_RESOLVEDBY = "resolvedBy";
    public static final String QUERY_RESOLVED_DATE = "resolvedDate";
    public static final String QUERY_TESTED_BY = "testedBy";
    public static final String QUERY_TESTED_DATE = "testedDate";
    public static final String QUERY_TREAT_AS_DEFERRED = "treatAsDeferred";

    public static final String COLUMN_BUG_REPORT_ID = "BUG_REPORT_ID";
    public static final String COLUMN_PROGRAM_NAME = "NAME";
    public static final String COLUMN_RELEASE = "RELEASE";
    public static final String COLUMN_VERSION = "VERSION";
    public static final String COLUMN_REPORT_TYPE = "REPORT_TYPE";
    public static final String COLUMN_SEVERITY = "SEVERITY";
    public static final String COLUMN_ATTACHMENTS = "ATTACHMENTS";
    public static final String COLUMN_ATTACHMENT_DESC = "ATTACHMENT_DESC";
    public static final String COLUMN_PROBLEM_SUMMARY = "PROBLEM_SUMMARY";
    public static final String COLUMN_REPRODUCIBLE = "REPRODUCIBLE";
    public static final String COLUMN_PROBLEM_DESCRIPTION = "PROBLEM_DESCRIPTION";
    public static final String COLUMN_SUGGESTED_FIX = "SUGGESTED_FIX";
    public static final String COLUMN_REPORTED_BY = "REPORTED_BY";
    public static final String COLUMN_REPORTED_DATE = "REPORTED_DATE";
    public static final String COLUMN_FUNCTIONAL_AREA = "FUNCTIONAL_AREA";
    public static final String COLUMN_ASSIGNED_TO = "ASSIGNED_TO";
    public static final String COLUMN_COMMENTS = "COMMENTS";
    public static final String COLUMN_STATUS = "STATUS";
    public static final String COLUMN_PRIORITY = "PRIORITY";
    public static final String COLUMN_RESOLUTION = "RESOLUTION";
    public static final String COLUMN_RESOLUTION_VERSION = "RESOLUTION_VERSION";
    public static final String COLUMN_RESOLVED_BY = "RESOLVED_BY";
    public static final String COLUMN_RESOLVED_DATE = "RESOLVED_DATE";
    public static final String COLUMN_TESTED_BY = "TESTED_BY";
    public static final String COLUMN_TESTED_DATE = "TESTED_DATE";
    public static final String COLUMN_TREAT_AS_DEFERRED = "TREAT_AS_DEFERRED";
    public static final String COLUMN_PROGRAM_ID = "ID";

    public static final String INSERT_NEW_BUG_REPORT = "INSERT INTO BUG_REPORTS(PROGRAM_ID,REPORT_TYPE,SEVERITY,ATTACHMENTS,ATTACHMENT_DESC,PROBLEM_SUMMARY,REPRODUCIBLE,PROBLEM_DESCRIPTION,SUGGESTED_FIX,REPORTED_BY,REPORTED_DATE,FUNCTIONAL_AREA,ASSIGNED_TO,COMMENTS,STATUS,PRIORITY,RESOLUTION,RESOLUTION_VERSION,RESOLVED_BY,RESOLVED_DATE,TESTED_BY,TESTED_DATE,TREAT_AS_DEFERRED)\n" +
            "VALUES (:programId,:reportType,:severity,:attachments,:attachmentDesc,:problemSummary,:reproducible,:problemDescription,:suggestedFix,:reportedBy,:reportedDate,:functionalArea,:assignedTo,:comments,:status,:priority,:resolution,:resolutionId,:resolvedBy,:resolvedDate,:testedBy,:testedDate,:treatAsDeferred);";
    public static final String GET_LAST_BUG_REPORT_ID = "select max(BUG_REPORT_ID) from BUG_REPORTS;";

    public static final String UPDATE_BUG_REPORT = "UPDATE BUG_REPORTS SET REPORT_TYPE=:reportType,SEVERITY=:severity,ATTACHMENTS=:attachments,ATTACHMENT_DESC=:attachmentDesc,PROBLEM_SUMMARY=:problemSummary,REPRODUCIBLE=:reproducible,PROBLEM_DESCRIPTION=:problemDescription,SUGGESTED_FIX=:suggestedFix,FUNCTIONAL_AREA=:functionalArea,ASSIGNED_TO=:assignedTo,COMMENTS=:comments,STATUS=:status,PRIORITY=:priority,RESOLUTION=:resolution,RESOLUTION_VERSION=:resolutionId,RESOLVED_BY=:resolvedBy,RESOLVED_DATE=:resolvedDate,TESTED_BY=:testedBy,TESTED_DATE=:testedDate,TREAT_AS_DEFERRED=:treatAsDeferred\n" +
            "WHERE BUG_REPORT_ID=:bugReportId;";

    public static final String GET_USERS_BUG_REPORTS = """
            select BUG_REPORT_ID, PROGRAMS.NAME as NAME, PROGRAMS.`RELEASE` as `RELEASE`, PROGRAMS.version as VERSION,REPORT_TYPE,SEVERITY,ATTACHMENTS, ATTACHMENT_DESC,PROBLEM_SUMMARY,REPRODUCIBLE,PROBLEM_DESCRIPTION,SUGGESTED_FIX, REPORTED_BY,REPORTED_DATE, FUNCTIONAL_AREA,ASSIGNED_TO,COMMENTS,STATUS,PRIORITY,RESOLUTION,RESOLUTION_VERSION,RESOLVED_BY,RESOLVED_DATE,TESTED_BY,TESTED_DATE, TREAT_AS_DEFERRED
                from BUG_REPORTS
                    inner join PROGRAMS
                           on BUG_REPORTS.PROGRAM_ID=PROGRAMS.ID
                    inner join ACCOUNTS
                           on BUG_REPORTS.REPORTED_BY=ACCOUNTS.USERNAME
                WHERE ACCOUNTS.USERNAME=:reportedBy
                ORDER BY BUG_REPORTS.REPORTED_BY DESC;""";

    public static final String GET_ALL_BUG_REPORTS = """
            select BUG_REPORT_ID, PROGRAMS.NAME as NAME, PROGRAMS.`RELEASE` as `RELEASE`, PROGRAMS.version as VERSION,REPORT_TYPE,SEVERITY,ATTACHMENTS, ATTACHMENT_DESC,PROBLEM_SUMMARY,REPRODUCIBLE,PROBLEM_DESCRIPTION,SUGGESTED_FIX, REPORTED_BY,REPORTED_DATE, FUNCTIONAL_AREA,ASSIGNED_TO,COMMENTS,STATUS,PRIORITY,RESOLUTION,RESOLUTION_VERSION,RESOLVED_BY,RESOLVED_DATE,TESTED_BY,TESTED_DATE, TREAT_AS_DEFERRED
            from BUG_REPORTS
                inner join PROGRAMS
                       on BUG_REPORTS.PROGRAM_ID=PROGRAMS.ID
                inner join ACCOUNTS
                       on BUG_REPORTS.REPORTED_BY=ACCOUNTS.USERNAME
            WHERE STATUS='OPEN'
            ORDER BY BUG_REPORTS.REPORTED_BY DESC;""";
    public static final String GET_PROGRAM_DATA = "SELECT * FROM PROGRAMS GROUP BY NAME,`RELEASE`,VERSION ORDER BY NAME,`RELEASE`,VERSION";
    public static final String GET_REPORT_TYPES = "SELECT * FROM REPORT_TYPE";
    public static final String GET_RESOLUTIONS = "SELECT * FROM RESOLUTION";
    public static final String GET_EMPLOYEES = "SELECT USERNAME FROM ACCOUNTS WHERE AUTHORITY<>'USER' AND AUTHORITY<>'ADMIN'";
}
















