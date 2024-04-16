package com.cecs544.BugReporter.views;

import com.cecs544.BugReporter.components.BugForm;
import com.cecs544.BugReporter.dao.BugReportDao;
import com.cecs544.BugReporter.enums.Role;
import com.cecs544.BugReporter.login.SecurityService;
import com.cecs544.BugReporter.model.BugData;
import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.util.Validator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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


@Route(value="", layout = MainLayout.class)
@PageTitle("Report Bug")
@PermitAll
@UIScope
public class ReportBugView extends VerticalLayout {
    @Autowired
    private BugReportDao bugReportDao;
    @Autowired
    SecurityService securityService;

    private Validator dataValidator = new Validator();

    private Button submit = new Button(Constants.SUBMIT);

    public ReportBugView() {
        addClassName("ReportBugView");
        setAlignItems(Alignment.CENTER);
        add(new H1("Report A Bug"));
    }

    @PostConstruct
    public void finishSetup() {
        BugForm form;
        UserDetails user = securityService.getAuthenticatedUser();
        boolean isUser = user.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("USER"));
        Role userRole = Validator.determineUserType(user.getAuthorities().toArray()[0].toString());
        Map<String,Map<String,Map<String,Integer>>> programData = bugReportDao.getProgramData();
        List<String> reportTypes = bugReportDao.getReportTypes();
        List<String> resolutions = bugReportDao.getResolutions();
        List<String> employees = bugReportDao.getEmployees();
        form = new BugForm(programData,reportTypes,resolutions,employees,isUser,user,userRole,true);
        add(form);
        add(submit);

        submit.addClickListener(click->{
            try{
                BugData bugData = form.getBugData();
                String errors=null;
                if((errors = dataValidator.validateInitialSubmission(bugData)) != null){
                    Notification.show(errors);
                }else{
                    Notification.show("Success");
                    bugReportDao.addNewBugReport(bugData);
                }
            } catch (IllegalStateException e){
                e.printStackTrace();
                Notification.show("Some how you have managed to break my enums. Congrats....");
            } catch (DataAccessException dataAccessException){
                dataAccessException.printStackTrace();
                Notification.show("There was an error submitting your bug report. Please try again later.");
            }
        });
    }
}


























