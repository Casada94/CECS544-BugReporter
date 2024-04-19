package com.cecs544.BugReporter.model;

import java.util.Objects;
import java.util.Set;

public class Program {
    private Integer ID ;
    private String NAME;
    private String release;
    private String version;
    Set<String> functionList;

    public Program() {
        ID  = null;
        NAME = null;
        release = null;
        version = null;
        functionList = null;
    }
    public Program(Integer id) {
        ID  = id;
        NAME = null;
        release = null;
        version = null;
        functionList = null;
    }
    public Program(Integer ID, String NAME, String release, String version, Set<String> function) {
        this.ID = ID;
        this.NAME = NAME;
        this.release = release;
        this.version = version;
        this.functionList = function;
    }
    public Program(Program other) {
        this.ID = other.ID;
        this.NAME = other.NAME;
        this.release = other.release;
        this.version = other.version;
        this.functionList = other.functionList;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<String> getFunction() {
        return functionList;
    }

    public void setFunction(Set<String> function) {
        this.functionList = function;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return Objects.equals(ID, program.ID) || (Objects.equals(NAME, program.NAME) && Objects.equals(release, program.release) && Objects.equals(version, program.version) );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }
}
