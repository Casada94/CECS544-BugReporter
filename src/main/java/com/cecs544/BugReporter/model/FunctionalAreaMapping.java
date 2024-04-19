package com.cecs544.BugReporter.model;

public class FunctionalAreaMapping {
    int programId;
    String functionalArea;

    public FunctionalAreaMapping(int programId, String functionalArea) {
        this.programId = programId;
        this.functionalArea = functionalArea;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        this.functionalArea = functionalArea;
    }
}
