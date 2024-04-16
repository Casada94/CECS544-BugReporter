package com.cecs544.BugReporter.enums;

import com.cecs544.BugReporter.util.Constants;

public enum Severity {
    FATAL(Constants.FATAL),
    SERIOUS(Constants.SERIOUS),
    MINOR(Constants.MINOR);

    private final String severity;

    Severity(String severity) {
        this.severity = severity;
    }

    public String getSeverity() {
        return severity;
    }
}
