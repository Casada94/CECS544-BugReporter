package com.cecs544.BugReporter.views;

import com.cecs544.BugReporter.components.BugForm;
import com.cecs544.BugReporter.dao.BugReportDao;
import com.cecs544.BugReporter.enums.Role;
import com.cecs544.BugReporter.login.SecurityService;
import com.cecs544.BugReporter.model.BugData;
import com.cecs544.BugReporter.model.Program;
import com.cecs544.BugReporter.util.AwsS3Util;
import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.util.Validator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Report Bug")
@PermitAll
@UIScope
public class ReportBugView extends VerticalLayout implements BeforeEnterObserver {
    @Autowired
    private BugReportDao bugReportDao;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private AwsS3Util awsS3Util;

    private BugForm form;
    private Button submit = new Button(Constants.SUBMIT);
    private Map<String, Map<String, Map<String, List<String>>>> programData;
    private List<String> reportTypes;
    private List<String> resolutions;
    private List<String> employees;
    UserDetails user;
    boolean isUser;
    Role userRole;

    public ReportBugView() {
        addClassName("ReportBugView");
        setAlignItems(Alignment.CENTER);
        add(new H1("Report A Bug"));
    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(bugReportDao.passwordChangeRequired(securityService.getAuthenticatedUser().getUsername())){
            beforeEnterEvent.forwardTo("changePassword");
        }
    }
    @PostConstruct
    public void finishSetup() {

        user = securityService.getAuthenticatedUser();
        isUser = user.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("USER"));
        userRole = Validator.determineUserType(user.getAuthorities().toArray()[0].toString());
        programData = bugReportDao.getProgramData();
        reportTypes = bugReportDao.getReportTypes();
        resolutions = bugReportDao.getResolutions();
        employees = bugReportDao.getEmployees();
        form = new BugForm(programData, reportTypes, resolutions, employees, isUser, user, userRole, true);
        add(form);
        add(submit);

        setSubmitListener(submit);
    }

    public void refreshForm() {
        BugForm newForm = new BugForm(programData, reportTypes, resolutions, employees, isUser, user, userRole, true);
        replace(form, newForm);
        form = newForm;
        Button newSubmit = new Button(Constants.SUBMIT);
        replace(submit, newSubmit);
        submit = newSubmit;
        setSubmitListener(submit);
    }


    public void setSubmitListener(Button submitBtn) {
        submitBtn.addClickListener(click -> {
            try {
                BugData bugData = form.getBugData();
                bugData.setProgramId(findId(bugData.getProgramName(),bugData.getRelease(),bugData.getVersion()));
                MultiFileBuffer buffer = null;
                if (bugData.isAttachments()) {
                    if (bugData.getAttachmentDesc() == null || bugData.getAttachmentDesc().isEmpty()) {
                        Notification.show("Please provide a description for your attachment.");
                        return;
                    } else {
                        buffer = form.getMultiFileBuffer();
                        if (buffer.getFiles().isEmpty()) {
                            Notification.show("Please provide an attachment.");
                            return;
                        }
                    }
                }
                String errors = null;
                if ((errors = Validator.validateInitialSubmission(bugData)) != null) {
                    Notification.show(errors);
                } else {
                    Notification.show("Success");
                    bugData.setBugReportId(bugReportDao.addNewBugReport(bugData));
                    if (buffer != null) {
                        awsS3Util.upload(buffer, bugData.getBugReportId());
                    }
                }
                refreshForm();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Notification.show("Some how you have managed to break my enums. Congrats....");
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
                Notification.show("There was an error submitting your bug report. Please try again later.");
            }
        });
    }

    public Integer findId(String programName, String release, String version){
        List<Program> programs = bugReportDao.getPrograms();

        for(Program program : programs){
            if(program.getNAME().equals(programName) && program.getRelease().equals(release) && program.getVersion().equals(version)){
                return program.getID();
            }
        }
        return null;
    }
}


























