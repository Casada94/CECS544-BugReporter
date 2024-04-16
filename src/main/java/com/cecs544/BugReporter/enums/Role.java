package com.cecs544.BugReporter.enums;

import com.cecs544.BugReporter.util.Constants;

public enum Role {
    USER(Constants.USER),
    ADMIN(Constants.ADMIN),
    EMPLOYEE(Constants.EMPLOYEE),
    MANAGER(Constants.MANAGER);

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
