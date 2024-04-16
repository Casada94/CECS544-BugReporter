package com.cecs544.BugReporter.enums;

import com.cecs544.BugReporter.util.Constants;

public enum Status {
    OPEN(Constants.OPEN),
    CLOSED(Constants.CLOSED);

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
