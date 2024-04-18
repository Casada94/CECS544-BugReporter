package com.cecs544.BugReporter.views;

import com.cecs544.BugReporter.dao.BugReportDao;
import com.cecs544.BugReporter.login.SecurityService;
import com.cecs544.BugReporter.util.Constants;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Route(value = "changePassword")
@PageTitle("Change Password")
@PermitAll
@UIScope
public class ChangePasswordView extends VerticalLayout {
    @Autowired
    private BugReportDao bugReportDao;
    @Autowired
    private SecurityService securityService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private PasswordField oldPassword = new PasswordField("Old Password");
    private PasswordField newPassword = new PasswordField("New Password");
    private PasswordField confirmPassword = new PasswordField("Confirm Password");

    private Button submit = new Button(Constants.SUBMIT);

    public ChangePasswordView() {
        addClassName("ChangePasswordView");
        setAlignItems(Alignment.CENTER);
        add(new H1("Change Password"));
        add(oldPassword, newPassword, confirmPassword);
        add(submit);

    }

    @PostConstruct
    public void init(){

        UserDetails user = securityService.getAuthenticatedUser();

        submit.addClickListener(e -> {
            if (oldPassword.isEmpty() ){
                Notification.show("Old Password is required",5000, Notification.Position.MIDDLE);
                return;
            }else if( newPassword.isEmpty()){
                Notification.show("New Password is required",5000, Notification.Position.MIDDLE);
                return;
            } else if (confirmPassword.isEmpty()) {
                Notification.show("Confirmation of New Password is required",5000, Notification.Position.MIDDLE);
                return;
            }
            if(newPassword.getValue().length() < 8){
                Notification.show("Password must be at least 8 characters long",5000, Notification.Position.MIDDLE);
                return;
            } else if(newPassword.getValue().equals(user.getPassword())){
                Notification.show("New Password cannot be the same as the old password",5000, Notification.Position.MIDDLE);
                return;
            }
            if (!newPassword.getValue().equals(confirmPassword.getValue())) {
                Notification.show("New Password and Confirm Password do not match",5000, Notification.Position.MIDDLE);
                return;
            }
            try {
                String password = Constants.BCRYPT + bCryptPasswordEncoder.encode(newPassword.getValue());
                bugReportDao.changePassword(user.getUsername() , password);
                Notification.show("Password changed successfully",5000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error changing password",5000, Notification.Position.MIDDLE);
            }
            oldPassword.clear();
            newPassword.clear();
            confirmPassword.clear();
            UI.getCurrent().navigate("/");
        });

    }
}
