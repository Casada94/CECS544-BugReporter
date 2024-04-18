package com.cecs544.BugReporter.model;

import java.util.List;
import java.util.Map;

public class Program {
    private String ID ;
    private String NAME;
    private Map<String, Map<String,List<String>>> releaseVersionFunctionMap;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Map<String, Map<String, List<String>>> getReleaseVersionFunctionMap() {
        return releaseVersionFunctionMap;
    }
    public void setReleaseVersionFunctionMap(Map<String, Map<String, List<String>>> releaseVersionFunctionMap) {
        this.releaseVersionFunctionMap = releaseVersionFunctionMap;
    }
}
