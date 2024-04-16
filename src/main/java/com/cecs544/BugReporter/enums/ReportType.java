package com.cecs544.BugReporter.enums;

import com.cecs544.BugReporter.util.Constants;

public enum ReportType {

    CODDING_ERROR(Constants.CODING_ERROR),
    DESIGN_ERROR(Constants.DESIGN_ERROR),
    SUGGESTION(Constants.SUGGESTION),
    DOCUMENTATION(Constants.DOCUMENTATION),
    HARDWARE(Constants.HARDWARE),
    QUERY(Constants.QUERY),;

    private final String reportType;

    ReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportType() {
        return reportType;
    }
}
