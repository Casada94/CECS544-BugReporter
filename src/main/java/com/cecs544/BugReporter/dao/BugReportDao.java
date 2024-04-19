package com.cecs544.BugReporter.dao;

import com.cecs544.BugReporter.model.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public static final String GET_PROGRAMS_AND_FUNCTIONS = Constants.GET_PROGRAMS_AND_FUNCTIONS;
    public static final String GET_FUNCTIONAL_AREA_MAPPINGS = Constants.GET_FUNCTIONAL_AREA_MAPPINGS;

    @Cacheable("programData")
    public Map<String, Map<String, Map<String, List<String>>>> getProgramData() {
        Map<String, Map<String, Map<String, List<String>>>> programData = new HashMap<>();
        try {
            jdbcTemplate.query(GET_PROGRAMS_AND_FUNCTIONS, rs -> {
                int id = rs.getInt(Constants.COLUMN_ID);
                String programName = rs.getString(Constants.COLUMN_PROGRAM_NAME);
                String release = rs.getString(Constants.COLUMN_RELEASE);
                String version = rs.getString(Constants.COLUMN_VERSION);
                String functionalArea = rs.getString(Constants.COLUMN_AREA);
                if (!programData.containsKey(programName)) {
                    Map<String,Map<String,List<String>>> releaseData = new HashMap<>();
                    Map<String,List<String>> versionData = new HashMap<>();
                    List<String> functionalAreas = new ArrayList<>();
                    functionalAreas.add(functionalArea);
                    versionData.put(version, functionalAreas);
                    releaseData.put(release, versionData);
                    programData.put(programName,releaseData );
                } else if(!programData.get(programName).containsKey(release)){
                    Map<String,List<String>> versionData = new HashMap<>();
                    List<String> functionalAreas = new ArrayList<>();
                    functionalAreas.add(functionalArea);
                    versionData.put(version, functionalAreas);
                    programData.get(programName).put(release, versionData);
                } else if(!programData.get(programName).get(release).containsKey(version)){
                    List<String> functionalAreas = new ArrayList<>();
                    functionalAreas.add(functionalArea);
                    programData.get(programName).get(release).put(version, functionalAreas);
                } else {
                    programData.get(programName).get(release).get(version).add(functionalArea);
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
            bugReport.setProgramId(rs.getInt(Constants.COLUMN_ID));
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


    public boolean passwordChangeRequired(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(Constants.PASSWORD_CHANGE_REQUIRED, new String[]{username}, Boolean.class));
    }
    public void changePassword(String username, String newPassword) {
        jdbcTemplate.update(Constants.CHANGE_PASSWORD, newPassword, username);
    }
    public List<Account> getAccounts() {
        return jdbcTemplate.query(Constants.GET_ACCOUNTS, (rs, rowNum) -> {
            Account account = new Account();
            account.setUSERNAME(rs.getString(Constants.COLUMN_USERNAME));
            account.setFIRST_NAME(rs.getString(Constants.COLUMN_FIRST_NAME));
            account.setLAST_NAME(rs.getString(Constants.COLUMN_LAST_NAME));
            account.setAUTHORITY(rs.getString(Constants.COLUMN_AUTHORITY));
            account.setENABLED(rs.getBoolean(Constants.COLUMN_ENABLED));
            account.setPASSWORD_CHANGE_REQUIRED(rs.getBoolean(Constants.COLUMN_PASSWORD_CHANGE_REQUIRED));
            return account;
        });
    }
    public void addAccount(Account account) {
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.QUERY_FIRST_NAME, account.getFIRST_NAME());
        params.put(Constants.QUERY_LAST_NAME, account.getLAST_NAME());
        params.put(Constants.QUERY_PASSWORD, account.getPASSWORD());
        params.put(Constants.QUERY_AUTHORITY, account.getAUTHORITY());
        params.put(Constants.QUERY_USERNAME, account.getUSERNAME());
        namedParamJdbcTemplate.update(Constants.INSERT_ACCOUNT, params);
    }

    public void updateAccount(Account account) {
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.QUERY_FIRST_NAME, account.getFIRST_NAME());
        params.put(Constants.QUERY_LAST_NAME, account.getLAST_NAME());
        params.put(Constants.QUERY_AUTHORITY, account.getAUTHORITY());
        params.put(Constants.QUERY_ENABLED, account.getENABLED());
        params.put(Constants.QUERY_PASSWORD_CHANGE_REQUIRED, account.getPASSWORD_CHANGE_REQUIRED());
        params.put(Constants.QUERY_USERNAME, account.getUSERNAME());
        namedParamJdbcTemplate.update(Constants.UPDATE_ACCOUNT, params);
    }
    public void deleteAccount(Account account) {
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.QUERY_USERNAME, account.getUSERNAME());
        namedParamJdbcTemplate.update(Constants.DELETE_ACCOUNT, params);
    }

    public List<Program> getPrograms(){
        List<Program> programs = new ArrayList<>();
        jdbcTemplate.query(Constants.GET_PROGRAMS_AND_FUNCTIONS, (rs, rowNum) -> {
            Program program = new Program();
            program.setID(rs.getInt(Constants.COLUMN_ID));
            if(programs.contains(program)){
                programs.get(programs.indexOf(program)).getFunction().add(rs.getString(Constants.COLUMN_AREA));
            }
            else{
                program.setNAME(rs.getString(Constants.COLUMN_PROGRAM_NAME));
                program.setRelease(rs.getString(Constants.COLUMN_RELEASE));
                program.setVersion(rs.getString(Constants.COLUMN_VERSION));
                Set<String> functions = new HashSet<>();
                functions.add(rs.getString(Constants.COLUMN_AREA));
                program.setFunction(functions);
                programs.add(program);
            }

            return null;
        });
        return programs;
    }

    @Transactional
    public void removeFunctionalAreas(Program program) {
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.QUERY_PROGRAM_ID, program.getID());
        for(String functionalArea: program.getFunction()){
            params.put(Constants.QUERY_FUNCTIONAL_AREA, functionalArea);
            namedParamJdbcTemplate.update(Constants.DELETE_PROGRAM_FUNCTIONAL_AREAS, params);
        }
    }

    @Transactional
    public void addProgram(Program program) {
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.QUERY_PROGRAM_NAME, program.getNAME());
        params.put(Constants.QUERY_RELEASE, program.getRelease());
        params.put(Constants.QUERY_VERSION, program.getVersion());
        namedParamJdbcTemplate.update(Constants.INSERT_PROGRAM, params);
        program.setID(jdbcTemplate.queryForObject(Constants.GET_LAST_PROGRAM_ID,Integer.class));
        for(String functionalArea: program.getFunction()){
            params.put(Constants.QUERY_FUNCTIONAL_AREA, functionalArea);
            namedParamJdbcTemplate.update(Constants.INSERT_FUNCTIONAL_AREA, params);
        }
        for(String functionalArea: program.getFunction()){
            params.put(Constants.QUERY_PROGRAM_ID, program.getID());
            params.put(Constants.QUERY_FUNCTIONAL_AREA, functionalArea);
            namedParamJdbcTemplate.update(Constants.INSERT_PROGRAM_FUNCTIONAL_AREA, params);
        }
    }

    @Transactional
    public void updateProgram(Program program) {
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.QUERY_PROGRAM_NAME, program.getNAME());
        params.put(Constants.QUERY_RELEASE, program.getRelease());
        params.put(Constants.QUERY_VERSION, program.getVersion());
        params.put(Constants.QUERY_PROGRAM_ID, program.getID());
        namedParamJdbcTemplate.update(Constants.UPDATE_PROGRAM, params);
        for(String functionalArea: program.getFunction()){
            params.put(Constants.QUERY_FUNCTIONAL_AREA, functionalArea);
            namedParamJdbcTemplate.update(Constants.INSERT_FUNCTIONAL_AREA, params);
        }
        for(String functionalArea: program.getFunction()){
            params.put(Constants.QUERY_PROGRAM_ID, program.getID());
            params.put(Constants.QUERY_FUNCTIONAL_AREA, functionalArea);
            namedParamJdbcTemplate.update(Constants.INSERT_PROGRAM_FUNCTIONAL_AREA, params);
        }
    }

    public List<User> fetchEmployeeAccounts(){
        List<User> users = new ArrayList<>();
        jdbcTemplate.query(Constants.GET_EMPLOYEE_ACCOUNTS, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString(Constants.COLUMN_USERNAME));
            user.setFirstName(rs.getString(Constants.COLUMN_FIRST_NAME));
            user.setLastName(rs.getString(Constants.COLUMN_LAST_NAME));
            user.setAuthority(rs.getString(Constants.COLUMN_AUTHORITY));
            user.setEnabled(rs.getBoolean(Constants.COLUMN_ENABLED));
            user.setPasswordChangeRequired(rs.getBoolean(Constants.COLUMN_PASSWORD_CHANGE_REQUIRED));
            users.add(user);
            return null;
        });
        return users;
    }

    public List<FunctionalAreaMapping> fetchFunctionalAreaMappings() {
        List<FunctionalAreaMapping> mappings = new ArrayList<>();
        jdbcTemplate.query(GET_FUNCTIONAL_AREA_MAPPINGS, rs -> {
            int programId = rs.getInt(Constants.COLUMN_PROGRAM_ID);
            String functionalArea = rs.getString(Constants.COLUMN_FUNCTIONAL_AREA);
            mappings.add(new FunctionalAreaMapping(programId, functionalArea));
        });
        return mappings;
    }


    @Scheduled(cron = "${spring.cache.clearSchedule}")
    public void clearCache() {
        cacheManager.getCacheNames().forEach(cache -> cacheManager.getCache(cache).clear());
    }
}