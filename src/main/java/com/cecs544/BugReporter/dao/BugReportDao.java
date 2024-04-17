package com.cecs544.BugReporter.dao;

import com.cecs544.BugReporter.model.BugData;
import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.util.Validator;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableCaching
public class BugReportDao {
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    @Qualifier("namedParamJdbcTemplate")
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    public static final String INSERT_NEW_BUG_REPORT = Constants.INSERT_NEW_BUG_REPORT;
    public static final String UPDATE_BUG_REPORT = Constants.UPDATE_BUG_REPORT;
    public static final String GET_PROGRAM_DATA = Constants.GET_PROGRAM_DATA;
    public static final String GET_REPORT_TYPES = Constants.GET_REPORT_TYPES;
    public static final String GET_RESOLUTIONS = Constants.GET_RESOLUTIONS;
    public static final String GET_EMPLOYEES = Constants.GET_EMPLOYEES;

    @Cacheable("programData")
    public Map<String, Map<String, Map<String, Integer>>> getProgramData() {
        Map<String, Map<String, Map<String, Integer>>> programData = new HashMap<>();
        try {
            jdbcTemplate.query(GET_PROGRAM_DATA, rs -> {
                String programName = rs.getString(Constants.COLUMN_PROGRAM_NAME);
                String release = rs.getString(Constants.COLUMN_RELEASE);
                String version = rs.getString(Constants.COLUMN_VERSION);
                int id = rs.getInt(Constants.COLUMN_PROGRAM_ID);
                if (programData.containsKey(programName)) {
                    Map<String, Map<String, Integer>> releaseData = programData.get(programName);
                    if (releaseData.containsKey(release)) {
                        Map<String, Integer> versionData = releaseData.get(release);
                        versionData.put(version, id);
                    } else {
                        Map<String, Integer> versionData = new HashMap<>();
                        versionData.put(version, id);
                        releaseData.put(release, versionData);
                    }
                } else {
                    Map<String, Integer> versionData = new HashMap<>();
                    versionData.put(version, id);
                    Map<String, Map<String, Integer>> releaseData = new HashMap<>();
                    releaseData.put(release, versionData);
                    programData.put(programName, releaseData);
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
            Notification.show("Error getting program data");
        }
        return programData;
    }

    @Cacheable("resolutions")
    public List<String> getResolutions() {
        return jdbcTemplate.queryForList(GET_RESOLUTIONS, String.class);
    }

    @Cacheable("reportTypes")
    public List<String> getReportTypes() {
        return jdbcTemplate.queryForList(GET_REPORT_TYPES, String.class);
    }

    @Cacheable("employees")
    public List<String> getEmployees() {
        return jdbcTemplate.queryForList(GET_EMPLOYEES, String.class);
    }

    public int addNewBugReport(BugData bugReport) {
        Map<String, Object> params = buildParams(bugReport);
        namedParamJdbcTemplate.update(INSERT_NEW_BUG_REPORT, params);
        return jdbcTemplate.queryForObject(Constants.GET_LAST_BUG_REPORT_ID, Integer.class);
    }

    public void updateBugReport(BugData bugReport) {
        Map<String, Object> params = buildParams(bugReport);
        params.put(Constants.QUERY_BUG_REPORT_ID, bugReport.getBugReportId());
        namedParamJdbcTemplate.update(UPDATE_BUG_REPORT, params);
    }

    public Map<String, Object> buildParams(BugData bugReport) {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.QUERY_PROGRAM_ID, bugReport.getProgramId());
        params.put(Constants.QUERY_REPORT_TYPE, bugReport.getReportType().getReportType());
        params.put(Constants.QUERY_SEVERITY, bugReport.getSeverity().getSeverity());
        params.put(Constants.QUERY_ATTACHMENTS, bugReport.isAttachments());
        params.put(Constants.QUERY_ATTACHMENT_DESC, Validator.nullOrString(bugReport.getAttachmentDesc()));
        params.put(Constants.QUERY_PROBLEM_SUMMARY, bugReport.getProblemSummary());
        params.put(Constants.QUERY_REPRODUCIBLE, bugReport.getReproducible());
        params.put(Constants.QUERY_PROBLEM_DESCRIPTION, bugReport.getProblemDescription());
        params.put(Constants.QUERY_SUGGESTED_FIX, bugReport.getSuggestedFix());
        params.put(Constants.QUERY_REPORTED_BY, bugReport.getReportedBy());
        params.put(Constants.QUERY_REPORTED_DATE, bugReport.getReportedDate());
        params.put(Constants.QUERY_FUNCTIONAL_AREA, Validator.nullOrString(bugReport.getFunctionalArea()));
        params.put(Constants.QUERY_ASSIGNED_TO, bugReport.getAssignedTo());
        params.put(Constants.QUERY_COMMENTS, Validator.nullOrString(bugReport.getComments()));
        params.put(Constants.QUERY_STATUS, Validator.nullOrString(bugReport.getStatus().getStatus()));
        params.put(Constants.QUERY_PRIORITY, bugReport.getPriority());
        params.put(Constants.QUERY_RESOLUTION, Validator.nullOrString(bugReport.getResolution().getResolution()));
        params.put(Constants.QUERY_RESOLUTION_ID, bugReport.getResolutionID());
        params.put(Constants.QUERY_RESOLVEDBY, bugReport.getResolvedBy());
        params.put(Constants.QUERY_RESOLVED_DATE, bugReport.getResolvedDate());
        params.put(Constants.QUERY_TESTED_BY, bugReport.getTestedBy());
        params.put(Constants.QUERY_TESTED_DATE, bugReport.getTestedDate());
        params.put(Constants.QUERY_TREAT_AS_DEFERRED, bugReport.getTreatAsDeferred());
        return params;
    }

    public List<BugData> getBugReports(String username, boolean isUser) {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.QUERY_REPORTED_BY, username);

        String query = isUser ? Constants.GET_USERS_BUG_REPORTS : Constants.GET_ALL_BUG_REPORTS;

        return namedParamJdbcTemplate.query(query, params, (rs, rowNum) -> {
            BugData bugReport = new BugData();
            bugReport.setBugReportId(rs.getInt(Constants.COLUMN_BUG_REPORT_ID));
            bugReport.setProgramName(rs.getString(Constants.COLUMN_PROGRAM_NAME));
            bugReport.setRelease(rs.getString(Constants.COLUMN_RELEASE));
            bugReport.setVersion(rs.getString(Constants.COLUMN_VERSION));
            bugReport.setReportType(rs.getString(Constants.COLUMN_REPORT_TYPE));
            bugReport.setSeverity(rs.getString(Constants.COLUMN_SEVERITY));
            bugReport.setAttachments(rs.getBoolean(Constants.COLUMN_ATTACHMENTS));
            bugReport.setAttachmentDesc(rs.getString(Constants.COLUMN_ATTACHMENT_DESC));
            bugReport.setProblemSummary(rs.getString(Constants.COLUMN_PROBLEM_SUMMARY));
            bugReport.setReproducible(rs.getBoolean(Constants.COLUMN_REPRODUCIBLE));
            bugReport.setProblemDescription(rs.getString(Constants.COLUMN_PROBLEM_DESCRIPTION));
            bugReport.setSuggestedFix(rs.getString(Constants.COLUMN_SUGGESTED_FIX));
            bugReport.setReportedBy(rs.getString(Constants.COLUMN_REPORTED_BY));
            bugReport.setReportedDate(rs.getDate(Constants.COLUMN_REPORTED_DATE));
            bugReport.setFunctionalArea(rs.getString(Constants.COLUMN_FUNCTIONAL_AREA));
            bugReport.setAssignedTo(rs.getString(Constants.COLUMN_ASSIGNED_TO));
            bugReport.setComments(rs.getString(Constants.COLUMN_COMMENTS));
            bugReport.setStatus(rs.getString(Constants.COLUMN_STATUS));
            bugReport.setPriority(rs.getInt(Constants.COLUMN_PRIORITY));
            bugReport.setResolution(rs.getString(Constants.COLUMN_RESOLUTION));
            bugReport.setResolutionVersion(rs.getString(Constants.COLUMN_RESOLUTION_VERSION));
            bugReport.setResolvedBy(rs.getString(Constants.COLUMN_RESOLVED_BY));
            bugReport.setResolvedDate(rs.getDate(Constants.COLUMN_RESOLVED_DATE));
            bugReport.setTestedBy(rs.getString(Constants.COLUMN_TESTED_BY));
            bugReport.setTestedDate(rs.getDate(Constants.COLUMN_TESTED_DATE));
            bugReport.setTreatAsDeferred(rs.getBoolean(Constants.COLUMN_TREAT_AS_DEFERRED));
            return bugReport;
        });
    }


    @Scheduled(cron = "${spring.cache.clearSchedule}")
    public void clearCache() {
        cacheManager.getCacheNames().forEach(cache -> cacheManager.getCache(cache).clear());
    }
}
