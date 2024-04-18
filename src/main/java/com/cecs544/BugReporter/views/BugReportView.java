package com.cecs544.BugReporter.views;

import com.cecs544.BugReporter.components.BugForm;
import com.cecs544.BugReporter.dao.BugReportDao;
import com.cecs544.BugReporter.enums.Role;
import com.cecs544.BugReporter.login.SecurityService;
import com.cecs544.BugReporter.model.BugData;
import com.cecs544.BugReporter.util.AwsS3Util;
import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.util.Validator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Route(value = "yourBugReports", layout = MainLayout.class)
@PageTitle("Your Bug Reports")
@PermitAll
@UIScope
public class BugReportView extends VerticalLayout implements BeforeEnterObserver {
    @Autowired
    private BugReportDao bugReportDao;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private AwsS3Util awsS3Util;

    private Grid<BugData> grid = new Grid<>(BugData.class, false);
    private TabSheet tabSheet = new TabSheet();
    private Tab tab = new Tab();
    private BugForm bugForm = null;
    private Button update = new Button(Constants.UPDATE);
    private Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
    private List<BugData> reports;
    private Select<String> uploadAttachment;
    private Button downloadButton;
    private Map<String, Map<String, Map<String, Integer>>> programData;
    private List<String> reportTypes;
    private List<String> resolutions;
    private List<String> employees;
    private UserDetails user;
    private boolean isUser;
    private Role userRole;
    private Anchor previous;
    private List<String> fileList;

    public BugReportView() {
        addClassName("ReportBugView");
        setAlignItems(Alignment.CENTER);
        add(new H1("Review Bug Reports"));
        add(refreshButton);

        update.addClickListener(click -> {
            BugData bugData = bugForm.getBugData();
            MultiFileBuffer buffer = null;
            if (bugData.isAttachments()) {
                if (bugForm.isInitialSubmission()) {
                    if (bugData.getAttachmentDesc() == null || bugData.getAttachmentDesc().isEmpty()) {
                        Notification.show("Please provide a description for your attachment.");
                        return;
                    } else {
                        buffer = bugForm.getMultiFileBuffer();
                        if (buffer.getFiles().isEmpty()) {
                            Notification.show("Please provide an attachment.");
                            return;
                        }
                    }
                } else {
                    if (bugData.getAttachmentDesc() == null || bugData.getAttachmentDesc().isEmpty()) {
                        Notification.show("Please provide a description for your attachment.");
                        return;
                    } else {
                        buffer = bugForm.getMultiFileBuffer();
                        if (buffer.getFiles().isEmpty() && bugForm.attachmentListIsEmpty()) {
                            Notification.show("Please provide an attachment.");
                            return;
                        }
                    }
                }

            } else {
                bugData.setAttachmentDesc(null);
                if (!bugForm.attachmentListIsEmpty()) {
                    awsS3Util.deleteAllFiles(String.valueOf(bugData.getBugReportId()), fileList);
                }
            }
            bugReportDao.updateBugReport(bugData);
            if (buffer != null) {
                awsS3Util.upload(buffer, bugData.getBugReportId());
            }
            reports.set(reports.indexOf(bugData), bugData);
            Notification.show("Bug Report Updated", 5000, Notification.Position.MIDDLE);
        });

        grid.addItemClickListener(item -> {
            tabSheet.setVisible(true);
            update.setVisible(true);
            bugForm.updateForm(item.getItem());
            if (bugForm.hasAttachments()) {
                fileList = awsS3Util.getFileList(item.getItem().getBugReportId());
                bugForm.setUploadList(fileList);
            }
            if (previous != null) {
                remove(previous);
            }
        });


    }

    @PostConstruct
    private void finsihSetup() {
        user = securityService.getAuthenticatedUser();
        isUser = user.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("USER"));
        userRole = Validator.determineUserType(user.getAuthorities().toArray()[0].toString());
        reports = new ArrayList<>();
        reports = bugReportDao.getBugReports(securityService.getAuthenticatedUser().getUsername(), isUser);
//        grid.setItems(new ListDataProvider<>(reports));

        configureGrid(isUser, reports);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setHeight("500px");
        grid.setWidthFull();
        add(grid);

        programData = bugReportDao.getProgramData();
        reportTypes = bugReportDao.getReportTypes();
        resolutions = bugReportDao.getResolutions();
        employees = bugReportDao.getEmployees();
        bugForm = new BugForm(programData, reportTypes, resolutions, employees, isUser, user, userRole, false);
        uploadAttachment = bugForm.getUploadAttachmentSelect();
        downloadButton = bugForm.getDownloadAttachmentsButton();
        downloadClickListener(downloadButton);


        tab.add(bugForm);
        tabSheet.add("Bug Report", tab);
        tabSheet.setVisible(false);
        update.setVisible(false);
        tabSheet.setWidthFull();
        add(tabSheet);

        add(update);

        refreshButton.addClickListener(click -> {
            reports = bugReportDao.getBugReports(securityService.getAuthenticatedUser().getUsername(), isUser);
            if (update.isVisible()) {
                refreshForm();
            }
            grid.setItems(new ListDataProvider<>(reports));
        });

    }

    private void configureGrid(boolean isUser, List<BugData> reports) {
        grid.addClassName("BugData");
        grid.setSizeFull();
        if (isUser) {
            grid.setColumns("bugReportId", "programName", "release", "version", "reportType",
                    "severity", "attachments", "attachmentDesc", "problemSummary", "reproducible",
                    "problemDescription", "suggestedFix", "reportedBy", "reportedDate",
                    "functionalArea", "assignedTo", "comments", "status", "priority", "resolution",
                    "resolutionVersion", "resolvedBy", "resolvedDate", "testedBy", "testedDate",
                    "treatAsDeferred");
        } else {
            Grid.Column<BugData> bugIdColumn = grid.addColumn(BugData::getBugReportId);
            Grid.Column<BugData> programNameColumn = grid.addColumn(BugData::getProgramName);
            Grid.Column<BugData> reportedDateColumn = grid.addColumn(BugData::getReportedDate);
            Grid.Column<BugData> statusColumn = grid.addColumn(BugData::getStatus);
            Grid.Column<BugData> resolutionColumn = grid.addColumn(BugData::getResolution);
            Grid.Column<BugData> resolvedDateColumn = grid.addColumn(BugData::getResolvedDate);

            GridListDataView<BugData> bugDataView = grid.setItems(reports);
            BugDataFilter filter = new BugDataFilter(bugDataView);
            grid.getHeaderRows().clear();
            HeaderRow headerRow = grid.appendHeaderRow();
            headerRow.getCell(bugIdColumn).setComponent(createFilterHeader("Bug ID", filter::setBugId));
            headerRow.getCell(programNameColumn).setComponent(createFilterHeader("Program Name", filter::setProgramName));
            headerRow.getCell(reportedDateColumn).setComponent(createFilterHeader("Reported Date", filter::setReportedDate));
            headerRow.getCell(statusColumn).setComponent(createFilterHeader("Status", filter::setStatus));
            headerRow.getCell(resolutionColumn).setComponent(createFilterHeader("Resolution", filter::setResolution));
            headerRow.getCell(resolvedDateColumn).setComponent(createFilterHeader("Resolved Date", filter::setResolvedDate));
        }
//        grid.getColumns().forEach(col -> {
//            col.setAutoWidth(true);
//            col.setResizable(true);
////            col.setSortable(true);
//        });


    }

    private void refreshForm() {
        tabSheet.setVisible(false);
        update.setVisible(false);
        bugForm = new BugForm(programData, reportTypes, resolutions, employees, isUser, user, userRole, false);
        uploadAttachment = bugForm.getUploadAttachmentSelect();
        downloadButton = bugForm.getDownloadAttachmentsButton();
        downloadButton.setId("downloadButton");
        downloadClickListener(downloadButton);
        Tab tab2 = new Tab(bugForm);
        replace(tab, tab2);
        tab = tab2;
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

    private void downloadClickListener(Button downButton) {
        downButton.addClickListener(click -> {
            if (uploadAttachment.getValue().isEmpty()) {
                Notification.show("Please select a file to download");
                return;
            }
            File file = awsS3Util.getFile(bugForm.getBugReportId(), uploadAttachment.getValue());
            StreamResource streamResource = new StreamResource(file.getName(), () -> getStream(file));
            Anchor link = new Anchor(streamResource, String.format("%s (%d KB)", file.getName(),
                    (int) file.length() / 1024));
            link.getElement().setAttribute("download", true);
            this.add(link);
            link.getElement().callJsFunction("click");
            previous = link;
        });
    }

    private static Component createFilterHeader(String labelText,
                                                Consumer<String> filterChangeConsumer) {
        NativeLabel label = new NativeLabel(labelText);
        label.getStyle().set("padding-top", "var(--lumo-space-m)")
                .set("font-size", "var(--lumo-font-size-xs)");
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(label, textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");

        return layout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(bugReportDao.passwordChangeRequired(user.getUsername())){
            beforeEnterEvent.forwardTo("changePassword");
        }
    }

    private static class BugDataFilter {
        private final GridListDataView<BugData> dataView;

        private String BugReportId;
        private String ProgramName;
        private String Release;
        private String severity;
        private String reportedBy;
        private String reportedDate;
        private String functionalArea;
        private String assignedTo;
        private String status;
        private String priority;
        private String resolution;
        private String resolvedBy;
        private String resolvedDate;
        private String testedBy;
        private String testedDate;

        public BugDataFilter(GridListDataView<BugData> dataView) {
            this.dataView = dataView;
        }

        public void setBugId(String bugId) {
            this.BugReportId = bugId;
            dataView.refreshAll();
        }

        public void setProgramName(String programName) {
            this.ProgramName = programName;
            dataView.refreshAll();
        }

        public void setReportType(String reportType) {
            this.Release = reportType;
            dataView.refreshAll();
        }

        public void setReportedDate(String reportedDate) {
            this.reportedDate = reportedDate;
            dataView.refreshAll();
        }

        public void setSeverity(String severity) {
            this.severity = severity;
            dataView.refreshAll();
        }

        public void setReportedBy(String reportedBy) {
            this.reportedBy = reportedBy;
            dataView.refreshAll();
        }

        public void setFunctionalArea(String functionalArea) {
            this.functionalArea = functionalArea;
            dataView.refreshAll();
        }

        public void setAssignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
            dataView.refreshAll();
        }

        public void setStatus(String status) {
            this.status = status;
            dataView.refreshAll();
        }

        public void setPriority(String priority) {
            this.priority = priority;
            dataView.refreshAll();
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
            dataView.refreshAll();
        }

        public void setResolvedBy(String resolvedBy) {
            this.resolvedBy = resolvedBy;
            dataView.refreshAll();
        }

        public void setResolvedDate(String resolvedDate) {
            this.resolvedDate = resolvedDate;
            dataView.refreshAll();
        }

        public void setTestedBy(String testedBy) {
            this.testedBy = testedBy;
            dataView.refreshAll();
        }

        public void setTestedDate(String testedDate) {
            this.testedDate = testedDate;
            dataView.refreshAll();
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }

    }

}