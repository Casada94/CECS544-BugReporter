package com.cecs544.BugReporter.model;

import java.util.Objects;

public class Account {
    private String USERNAME;
    private String PASSWORD;
    private String FIRST_NAME;
    private String LAST_NAME ;
    private boolean ENABLED;
    private String AUTHORITY;
    private boolean PASSWORD_CHANGE_REQUIRED;

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    public boolean getENABLED() {
        return ENABLED;
    }

    public void setENABLED(boolean ENABLED) {
        this.ENABLED = ENABLED;
    }

    public String getAUTHORITY() {
        return AUTHORITY;
    }

    public void setAUTHORITY(String AUTHORITY) {
        this.AUTHORITY = AUTHORITY;
    }

    public boolean getPASSWORD_CHANGE_REQUIRED() {
        return PASSWORD_CHANGE_REQUIRED;
    }

    public void setPASSWORD_CHANGE_REQUIRED(boolean PASSWORD_CHANGE_REQUIRED) {
        this.PASSWORD_CHANGE_REQUIRED = PASSWORD_CHANGE_REQUIRED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(USERNAME, account.USERNAME);
    }

    @Override
    public int hashCode() {
        return Objects.hash(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, ENABLED, AUTHORITY, PASSWORD_CHANGE_REQUIRED);
    }
}
