package com.cecs544.BugReporter.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BugRepository {
    private Connection connection;  // Ensure this is initialized correctly elsewhere

    public BugRepository(Connection connection) {
        this.connection = connection;
    }

    public List<BugData> fetchAllBugs() {
        List<BugData> bugs = new ArrayList<>();
        String query = "SELECT * FROM bugs"; // Adjust this query based on your actual table schema

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                BugData bug = new BugData();
                // Assume the setters and getters are properly defined in the BugData class
                bug.setBugReportId(rs.getInt("BugReportId"));
                bug.setProgramName(rs.getString("ProgramName"));
                bug.setRelease(rs.getString("Release"));
                bug.setVersion(rs.getString("Version"));
                bug.setProgramId(rs.getInt("ProgramId"));
                bug.setReportType(rs.getString("ReportType"));
                bug.setSeverity(rs.getString("Severity"));
                bug.setAttachments(rs.getBoolean("Attachments"));
                bug.setAttachmentDesc(rs.getString("AttachmentDesc"));
                bug.setProblemSummary(rs.getString("ProblemSummary"));
                bug.setReproducible(rs.getBoolean("Reproducible"));
                bug.setProblemDescription(rs.getString("ProblemDescription"));
                bug.setSuggestedFix(rs.getString("SuggestedFix"));
                bug.setReportedBy(rs.getString("ReportedBy"));
                bug.setReportedDate(rs.getDate("ReportedDate"));
                bug.setFunctionalArea(rs.getString("FunctionalArea"));
                bug.setAssignedTo(rs.getString("AssignedTo"));
                bug.setComments(rs.getString("Comments"));
                bug.setStatus(rs.getString("Status"));
                bug.setPriority(rs.getInt("Priority"));
                bug.setResolution(rs.getString("Resolution"));
                bug.setResolutionRelease(rs.getString("ResolutionRelease"));
                bug.setResolutionVersion(rs.getString("ResolutionVersion"));
                bug.setResolutionId(rs.getInt("ResolutionId"));
                bug.setResolvedBy(rs.getString("ResolvedBy"));
                bug.setResolvedDate(rs.getDate("ResolvedDate"));
                bug.setTestedBy(rs.getString("TestedBy"));
                bug.setTestedDate(rs.getDate("TestedDate"));
                bug.setTreatAsDeferred(rs.getBoolean("TreatAsDeferred"));
                bugs.add(bug);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bugs;
    }

    public String bugToXml(BugData bug) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<BugData>" +
                "<BugReportId>" + bug.getBugReportId() + "</BugReportId>" +
                "<ProgramName>" + bug.getProgramName() + "</ProgramName>" +
                "<Release>" + bug.getRelease() + "</Release>" +
                "<Version>" + bug.getVersion() + "</Version>" +
                "<ProgramId>" + bug.getProgramId() + "</ProgramId>" +
                "<ReportType>" + (bug.getReportType() != null ? bug.getReportType().name() : "") + "</ReportType>" +
                "<Severity>" + (bug.getSeverity() != null ? bug.getSeverity().name() : "") + "</Severity>" +
                "<Attachments>" + bug.isAttachments() + "</Attachments>" +
                "<AttachmentDesc>" + bug.getAttachmentDesc() + "</AttachmentDesc>" +
                "<ProblemSummary>" + bug.getProblemSummary() + "</ProblemSummary>" +
                "<Reproducible>" + bug.getReproducible() + "</Reproducible>" +
                "<ProblemDescription>" + bug.getProblemDescription() + "</ProblemDescription>" +
                "<SuggestedFix>" + bug.getSuggestedFix() + "</SuggestedFix>" +
                "<ReportedBy>" + bug.getReportedBy() + "</ReportedBy>" +
                "<ReportedDate>" + (bug.getReportedDate() != null ? dateFormat.format(bug.getReportedDate()) : "") + "</ReportedDate>" +
                "<FunctionalArea>" + bug.getFunctionalArea() + "</FunctionalArea>" +
                "<AssignedTo>" + bug.getAssignedTo() + "</AssignedTo>" +
                "<Comments>" + bug.getComments() + "</Comments>" +
                "<Status>" + (bug.getStatus() != null ? bug.getStatus().name() : "") + "</Status>" +
                "<Priority>" + bug.getPriority() + "</Priority>" +
                "<Resolution>" + (bug.getResolution() != null ? bug.getResolution().name() : "") + "</Resolution>" +
                "<ResolutionRelease>" + bug.getResolutionRelease() + "</ResolutionRelease>" +
                "<ResolutionVersion>" + bug.getResolutionVersion() + "</ResolutionVersion>" +
                "<ResolutionId>" + bug.getResolutionID() + "</ResolutionId>" +
                "<ResolvedBy>" + bug.getResolvedBy() + "</ResolvedBy>" +
                "<ResolvedDate>" + (bug.getResolvedDate() != null ? dateFormat.format(bug.getResolvedDate()) : "") + "</ResolvedDate>" +
                "<TestedBy>" + bug.getTestedBy() + "</TestedBy>" +
                "<TestedDate>" + (bug.getTestedDate() != null ? dateFormat.format(bug.getTestedDate()) : "") + "</TestedDate>" +
                "<TreatAsDeferred>" + bug.getTreatAsDeferred() + "</TreatAsDeferred>" +
                "</BugData>";
    }
}
