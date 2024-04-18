package com.cecs544.BugReporter.views;

import com.cecs544.BugReporter.login.SecurityService;
import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.views.admin.AdminView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    private SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("BYOB: Bug Your Own Bug Reporter");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);
        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    private void createDrawer() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new RouterLink("Report A Bug", ReportBugView.class));
        verticalLayout.add(new RouterLink("Bug Reports", BugReportView.class));
        if (securityService.getAuthenticatedUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().contains(Constants.ADMIN))) {
            verticalLayout.add(new RouterLink("Admin", AdminView.class));
        }

        addToDrawer(verticalLayout);
    }

}
