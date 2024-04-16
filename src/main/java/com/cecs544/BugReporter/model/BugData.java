package com.cecs544.BugReporter.model;

import com.cecs544.BugReporter.enums.ReportType;
import com.cecs544.BugReporter.enums.Resolution;
import com.cecs544.BugReporter.enums.Severity;
import com.cecs544.BugReporter.enums.Status;
import com.cecs544.BugReporter.util.Constants;

import java.sql.Date;
import java.util.Objects;

public class BugData {

    private Integer BugReportId;
    private String ProgramName;
    private String Release;
    private String Version;
    private Integer ProgramId;
    private ReportType ReportType;
    private Severity Severity;
    private boolean Attachments;
    private String AttachmentDesc;
    private String ProblemSummary;
    private boolean Reproducible;
    private String ProblemDescription;
    private String SuggestedFix;
    private String ReportedBy;
    private Date ReportedDate;
    private String FunctionalArea;
    private String AssignedTo;
    private String Comments;
    private Status Status;
    private Integer Priority;
    private Resolution Resolution;
    private String ResolutionVersion;
    private String ResolvedBy;
    private Date ResolvedDate;
    private String TestedBy;
    private Date TestedDate;
    private boolean TreatAsDeferred;

    public BugData() {
    }

    /**
     * GETTERS
     **/
    public Integer getBugReportId() {return BugReportId;}
    public String getProgramName() {return ProgramName;}
    public String getRelease() {return Release;}
    public String getVersion() {return Version;}
    public Integer getProgramId(){return ProgramId;}
    public ReportType getReportType() {return ReportType;}
    public Severity getSeverity() {return Severity;}
    public boolean isAttachments() {return Attachments;}
    public String getAttachmentDesc() {return AttachmentDesc;}
    public String getProblemSummary() {return ProblemSummary;}
    public boolean getReproducible() {return Reproducible;}
    public String getProblemDescription() {return ProblemDescription;}
    public String getSuggestedFix() {return SuggestedFix;}
    public String getReportedBy() {return ReportedBy;}
    public Date getReportedDate() {return ReportedDate;}
    public String getFunctionalArea() {return FunctionalArea;}
    public String getAssignedTo() {return AssignedTo;}
    public String getComments() {return Comments;}
    public Status getStatus() {return Status;}
    public Resolution getResolution() {return Resolution;}
    public Integer getPriority() {return Priority;}
    public String getResolutionVersion() {return ResolutionVersion;}
    public String getResolvedBy() {return ResolvedBy;}
    public Date getResolvedDate() {return ResolvedDate;}
    public String getTestedBy() {return TestedBy;}
    public Date getTestedDate() {return TestedDate;}
    public boolean getTreatAsDeferred() {return TreatAsDeferred;}



    /**  SETTERS  **/
    public void setBugReportId(Integer bugReportId) {
        BugReportId = bugReportId;
    }
    public void setProgramName(String ProgramName) {this.ProgramName = ProgramName;}
    public void setRelease(String Release) {this.Release = Release;}
    public void setVersion(String Version) {this.Version = Version;}
    public void setProgramId(Integer id){this.ProgramId = id;}

    public void setReportType(String reportType) {
        switch(reportType){
            case Constants.CODING_ERROR:
                this.ReportType = com.cecs544.BugReporter.enums.ReportType.CODDING_ERROR;
                break;
            case Constants.DESIGN_ERROR:
                this.ReportType = com.cecs544.BugReporter.enums.ReportType.DESIGN_ERROR;
                break;
            case Constants.SUGGESTION:
                this.ReportType = com.cecs544.BugReporter.enums.ReportType.SUGGESTION;
                break;
            case Constants.DOCUMENTATION:
                this.ReportType = com.cecs544.BugReporter.enums.ReportType.DOCUMENTATION;
                break;
            case Constants.HARDWARE:
                this.ReportType = com.cecs544.BugReporter.enums.ReportType.HARDWARE;
                break;
            case Constants.QUERY:
                this.ReportType = com.cecs544.BugReporter.enums.ReportType.QUERY;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + reportType);
        }
    }

    public void setAttachments(boolean attachments) {this.Attachments = attachments;}

    public void setSeverity(String severity) {
        switch(severity){
            case Constants.FATAL:
                this.Severity = com.cecs544.BugReporter.enums.Severity.FATAL;
                break;
            case Constants.SERIOUS:
                this.Severity = com.cecs544.BugReporter.enums.Severity.SERIOUS;
                break;
            case Constants.MINOR:
                this.Severity = com.cecs544.BugReporter.enums.Severity.MINOR;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + severity);
        }
    }

    public void setAttachmentDesc(String AttachmentDesc) {this.AttachmentDesc = AttachmentDesc;}
    public void setProblemSummary(String ProblemSummary) {this.ProblemSummary = ProblemSummary;}
    public void setReproducible(boolean Reproducible) {this.Reproducible = Reproducible;}
    public void setProblemDescription(String ProblemDescription) {this.ProblemDescription = ProblemDescription;}
    public void setSuggestedFix(String SuggestedFix) {this.SuggestedFix = SuggestedFix;}
    public void setReportedBy(String ReportedBy) {this.ReportedBy = ReportedBy;}
    public void setReportedDate(Date ReportedDate) {this.ReportedDate = ReportedDate;}
    public void setFunctionalArea(String FunctionalArea) {this.FunctionalArea = FunctionalArea;}
    public void setAssignedTo(String AssignedTo) {this.AssignedTo = AssignedTo;}
    public void setComments(String Comments) {this.Comments = Comments;}
    public void setStatus(String status) {
        status = status == null ? Constants.EMPTY : status;
        switch(status){
            case Constants.OPEN:
            case Constants.EMPTY:
                this.Status = com.cecs544.BugReporter.enums.Status.OPEN;
                break;
            case Constants.CLOSED:
                this.Status = com.cecs544.BugReporter.enums.Status.CLOSED;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    public void setPriority(Integer Priority){
        this.Priority = Objects.requireNonNullElse(Priority, 3);
    }

    public void setResolution(String resolution) {
        resolution = resolution == null ? Constants.EMPTY : resolution;
        switch(resolution){
            case Constants.PENDING:
            case Constants.EMPTY:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.PENDING;
                break;
            case Constants.FIXED:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.FIXED;
                break;
            case Constants.IRREPRODUCIBLE:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.IRREPRODUCIBLE;
                break;
            case Constants.DEFERRED:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.DEFERRED;
                break;
            case Constants.AS_DESIGNED:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.AS_DESIGNED;
                break;
            case Constants.CANNOTFIX:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.CANNOTFIX;
                break;
            case Constants.WITHDRAWN_BY_REPORTER:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.WITHDRAWN_BY_REPORTER;
                break;
            case Constants.NEED_MORE_INFO:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.NEED_MORE_INFO;
                break;
            case Constants.DISAGREE_WITH_SUGGESTION:
                this.Resolution = com.cecs544.BugReporter.enums.Resolution.DISAGREE_WITH_SUGGESTION;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resolution);
        }
    }

    public void setResolutionVersion(String ResolutionVersion) {this.ResolutionVersion = ResolutionVersion;}
    public void setResolvedBy(String ResolvedBy) {this.ResolvedBy = ResolvedBy;}
    public void setResolvedDate(Date ResolvedDate) {this.ResolvedDate = ResolvedDate;}
    public void setTestedBy(String TestedBy) {this.TestedBy = TestedBy;}
    public void setTestedDate(Date TestedDate) {this.TestedDate = TestedDate;}
    public void setTreatAsDeferred(boolean TreatAsDeferred) {this.TreatAsDeferred = TreatAsDeferred;}

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        return Objects.equals(this.BugReportId, ((BugData) obj).BugReportId);
    }
}
