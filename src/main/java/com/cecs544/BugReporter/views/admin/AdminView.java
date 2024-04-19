package com.cecs544.BugReporter.views.admin;

import com.cecs544.BugReporter.dao.BugReportDao;
import com.cecs544.BugReporter.login.SecurityService;
import com.cecs544.BugReporter.model.Account;
import com.cecs544.BugReporter.model.FunctionalAreaMapping;
import com.cecs544.BugReporter.model.Program;
import com.cecs544.BugReporter.model.User;
import com.cecs544.BugReporter.util.XMLService;
import com.cecs544.BugReporter.views.MainLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Administration View")
@PermitAll
@UIScope
public class AdminView extends VerticalLayout implements BeforeEnterObserver {
    @Autowired
    private SecurityService securityService;
    @Autowired
    private BugReportDao bugReportDao;
    @Autowired
    private XMLService xmlService;
    @Value("${spring.roles}")
    private List<String> roles;

    private TabSheet tabSheet = new TabSheet();
    private AccountsTab accountsTab;
    private ProgramTab programTab;
    private UserDetails userDetails;
    private VerticalLayout downloadHolder = new VerticalLayout();

    @PostConstruct
    public void init() {
        accountsTab= new AccountsTab(this,bugReportDao.getAccounts(),roles);

        tabSheet.add("Accounts",accountsTab);
        this.userDetails = securityService.getAuthenticatedUser();
        setWidthFull();
        programTab = new ProgramTab(this,new ArrayList<>(bugReportDao.getPrograms()));
        tabSheet.add("Programs",programTab);
        tabSheet.setWidthFull();
        add(tabSheet);
        add(downloadHolder);

        tabSheet.addSelectedChangeListener(event -> {
            downloadHolder.removeAll();
        });

        Button addAccount = new Button("Add Account");
        Button updateAccount = new Button("Update Account");
        Button deleteAccount = new Button("Delete Account");
    }

    public List<String> getRoles() {
        return roles;
    }
    public List<Account> getAccounts() {
        return bugReportDao.getAccounts();
    }
    public void updateAccount(Account account) {
        bugReportDao.updateAccount(account);
    }
    public void addAccount(Account account) {
        bugReportDao.addAccount(account);
    }
    public void deleteAccount(Account account) {
        bugReportDao.deleteAccount(account);
    }

    public List<Program> getPrograms() {
        return bugReportDao.getPrograms();
    }

    public void addProgram(Program programs) {
        bugReportDao.addProgram(programs);
    }
    public void updateProgram(Program programs) {
        bugReportDao.updateProgram(programs);
    }
    public void removeFunctionalAreas(Program program) {
        bugReportDao.removeFunctionalAreas(program);
    }
    public void exportFunctionalAreas(){
        List<FunctionalAreaMapping> mappings = bugReportDao.fetchFunctionalAreaMappings();
        if(mappings == null || mappings.isEmpty()){
            Notification.show("No data to export",5000, Notification.Position.MIDDLE);
            return;
        }
        File file = xmlService.generateXML(mappings);
        StreamResource streamResource = new StreamResource(file.getName(), () -> getStream(file));
        Anchor link = new Anchor(streamResource, String.format("%s (%d KB)", file.getName(),
                (int) file.length() / 1024));
        link.getElement().setAttribute("download", true);
        downloadHolder.add(link);
        link.getElement().callJsFunction("click");
    }
    public void exportUsers(){
        List<User> mappings = bugReportDao.fetchEmployeeAccounts();
        if(mappings == null || mappings.isEmpty()){
            Notification.show("No data to export",5000, Notification.Position.MIDDLE);
            return;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File file = writeToFile(gson.toJson(mappings));
        StreamResource streamResource = new StreamResource(file.getName(), () -> getStream(file));
        Anchor link = new Anchor(streamResource, String.format("%s (%d KB)", file.getName(),
                (int) file.length() / 1024));
        link.getElement().setAttribute("download", true);
        downloadHolder.add(link);
        link.getElement().callJsFunction("click");
    }

    public File writeToFile(String json){
        try {
            File file = new File("employee.json");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream getStream(File file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stream;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"))){
            beforeEnterEvent.forwardTo("");
        }
    }
}
