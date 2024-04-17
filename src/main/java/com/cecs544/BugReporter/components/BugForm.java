package com.cecs544.BugReporter.components;

import com.cecs544.BugReporter.enums.Role;
import com.cecs544.BugReporter.model.BugData;
import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.util.Validator;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BugForm extends VerticalLayout{
    private Text name = new Text(Constants.COMPANY_NAME);
    private Text confidential = new Text(Constants.CONFIDENTIAL);
    private Text program = new Text(Constants.PROGRAM);
    private Select<String> programField = new Select<>();
    private Text release = new Text(Constants.RELEASE);
    private Select<String> releaseField = new Select<>();
    private Text version = new Text(Constants.VERSION);
    private Select<String> versionField = new Select<>();
    private Text problemNumber = new Text(Constants.PROBLEM_NUMBER);
    private IntegerField problemNumberField = new IntegerField();
    private Select<String> reportType = new Select<>();
    private Select<String> severity = new Select<>();
    private TextArea ifYesDescribeField = new TextArea(Constants.IF_YES_DESCRIBE);
    private MultiFileBuffer multiFileBuffer = new MultiFileBuffer();
    private Upload attachmentUpload = new Upload(multiFileBuffer);
    private Checkbox attachments = new Checkbox(Constants.ATTACHMENTS);
    private Select<String> uploadedAttachments = new Select<>();
    private Button downloadAttachments = new Button(VaadinIcon.DOWNLOAD.create()    );
    private TextField problemSummaryField = new TextField(Constants.PROBLEM_SUMMARY);
    private Text reproducible = new Text(Constants.REPRODUCIBLE);
    private Checkbox reproducibleCheckbox = new Checkbox();
    private TextArea problemDescAndReproduceField = new TextArea(Constants.PROBLEM_DESC_AND_REPRODUCE);
    private TextArea suggestedFixField = new TextArea(Constants.SUGGESTED_FIX);
    private TextField reportedByField = new TextField(Constants.REPORTED_BY);
    private DatePicker reportedDateField = new DatePicker(Constants.REPORTED_DATE);
    private TextField functionalAreaField = new TextField(Constants.FUNCTIONAL_AREA);
    private Select<String> assignedToField = new Select<>();
    private TextArea commentsField = new TextArea(Constants.COMMENTS);
    private Select<String> status = new Select<>();
    private Select<Integer> priority = new Select<>();
    private Select<String> resolution = new Select<>();
    private TextField resolutionVersion = new TextField();
    private Select<String> resolvedByField = new Select<>();
    private DatePicker resolvedDatePicker = new DatePicker(Constants.RESOLVED_DATE);
    private Select<String> testedByField = new Select<>();
    private DatePicker testedDatePicker = new DatePicker(Constants.TESTED_DATE);
    private Checkbox deferred = new Checkbox(Constants.TREAT_AS_DEFERRED);
    private Map<String,Map<String,Map<String,Integer>>> programData;
    private List<String> reportTypes;
    private List<String> resolutions;
    private boolean initial;

    public BugForm(Map<String,Map<String,Map<String,Integer>>> pData, List<String>rTypes,List<String>resolutionsFromDb,List<String>employees, boolean isUser, UserDetails user, Role userRole,boolean isInitial){
        setWidth("750px");
        setAlignItems(FlexComponent.Alignment.CENTER);
        programData = pData;
        reportTypes = rTypes;
        resolutions = resolutionsFromDb;
        initial = isInitial;

        problemNumberField.setReadOnly(true);
        reportedByField.setReadOnly(true);
        reportedDateField.setReadOnly(true);
        releaseField.setReadOnly(true);
        versionField.setReadOnly(true);

        if(initial){
            attachmentUpload.setVisible(false);
            uploadedAttachments.setVisible(false);
            downloadAttachments.setVisible(false);
            ifYesDescribeField.setVisible(false);
            attachments.addValueChangeListener(event -> {
                if (attachments.getValue()) {
                    attachmentUpload.setVisible(true);
                    ifYesDescribeField.setVisible(true);
                } else {
                    attachmentUpload.setVisible(false);
                    ifYesDescribeField.setVisible(false);
                }
            });
        }
        configureUpload();

        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setWidthFull();
        horizontalLayout1.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horizontalLayout1.add(new VerticalLayout(name), new VerticalLayout(confidential));
        problemNumberField.setWidth("75px");
        HorizontalLayout problemNumberLayout = new HorizontalLayout(problemNumber, problemNumberField);
        problemNumberLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        problemNumberLayout.setWidth("200px");
        horizontalLayout1.add(problemNumberLayout);
        add(horizontalLayout1);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setWidthFull();
        horizontalLayout2.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
//        programField.setLabel(Constants.PROGRAM);
        programField.setItems(new ListDataProvider<>(programData.keySet()));
        HorizontalLayout programLayout = new HorizontalLayout(program, programField);
        programLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout2.add(programLayout);
        releaseField.setWidth("75px");
        versionField.setWidth("75px");
        HorizontalLayout releaseLayout = new HorizontalLayout(release, releaseField);
        releaseLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout2.add(releaseLayout);
        HorizontalLayout versionLayout = new HorizontalLayout(version, versionField);
        horizontalLayout2.add(versionLayout);
        versionLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout2.setAlignItems(FlexComponent.Alignment.CENTER);
        add(horizontalLayout2);

        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setWidthFull();
        horizontalLayout3.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        reportType.setLabel(Constants.REPORT_TYPE);

        reportType.setItems(new ListDataProvider<>(reportTypes));
        severity.setItems(new ListDataProvider<>(Constants.severities));
        severity.setLabel(Constants.SEVERITY);
        horizontalLayout3.add(reportType, severity);
        add(horizontalLayout3);

        HorizontalLayout attachmentHorizontalLayout = new HorizontalLayout();
        attachmentHorizontalLayout.setWidthFull();
        attachmentHorizontalLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        attachmentHorizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("300px");
        verticalLayout.add(attachments);
        HorizontalLayout horizontalLayoutDownload = new HorizontalLayout();
        horizontalLayoutDownload.add(uploadedAttachments);
        horizontalLayoutDownload.add(downloadAttachments);
        verticalLayout.add(horizontalLayoutDownload);
        attachmentHorizontalLayout.add(verticalLayout);
        attachmentHorizontalLayout.add(attachmentUpload);
        ifYesDescribeField.setWidth("400px");
        ifYesDescribeField.setHeight("100px");
        attachmentHorizontalLayout.add(ifYesDescribeField);
        add(attachmentHorizontalLayout);

        problemSummaryField.setWidthFull();
        add(problemSummaryField);

        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setWidthFull();
        horizontalLayout5.setAlignItems(FlexComponent.Alignment.START);
        horizontalLayout5.add(reproducible,reproducibleCheckbox);
        add(horizontalLayout5);

        problemDescAndReproduceField.setWidthFull();
        problemDescAndReproduceField.setHeight(Constants.TEXT_AREA_HEIGHT);
        add(problemDescAndReproduceField);

        suggestedFixField.setWidthFull();
        suggestedFixField.setHeight(Constants.TEXT_AREA_HEIGHT);
        add(suggestedFixField);

        HorizontalLayout horizontalLayout8 = new HorizontalLayout();
        horizontalLayout8.setWidthFull();
        horizontalLayout8.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        reportedByField.setWidth(Constants.NAME_FIELD_WIDTH);
        reportedByField.setValue(user.getUsername());
        horizontalLayout8.add(reportedByField);
        reportedDateField.setValue(java.time.LocalDate.now());
        horizontalLayout8.add(reportedDateField);
        add(horizontalLayout8);

        if (!initial) {
            HorizontalLayout horizontalLayout9 = new HorizontalLayout();
            horizontalLayout9.add(new Text(Constants.FOR_OFFICE_USE_ONLY));
            add(horizontalLayout9);

            HorizontalLayout horizontalLayout10 = new HorizontalLayout();
            horizontalLayout10.setWidthFull();
            horizontalLayout10.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            functionalAreaField.setWidth("200px");
            horizontalLayout10.add(functionalAreaField);
            assignedToField.setWidth(Constants.NAME_FIELD_WIDTH);
            assignedToField.setLabel(Constants.ASSIGNED_TO);
            assignedToField.setItems(new ListDataProvider<>(employees));
            horizontalLayout10.add(assignedToField);
            add(horizontalLayout10);

            commentsField.setWidthFull();
            commentsField.setHeight(Constants.TEXT_AREA_HEIGHT);
            add(commentsField);

            HorizontalLayout horizontalLayout12 = new HorizontalLayout();
            horizontalLayout12.setWidthFull();
            horizontalLayout12.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
            status.setLabel(Constants.STATUS);
            status.setItems(new ListDataProvider<>(Constants.statuses));
            priority.setLabel(Constants.PRIORITY);
            priority.setItems(new ListDataProvider<>(Constants.priorities));
            horizontalLayout12.add(status,priority);
            add(horizontalLayout12);

            HorizontalLayout horizontalLayout16 = new HorizontalLayout();
            horizontalLayout16.setWidthFull();
            horizontalLayout16.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
            resolution.setLabel(Constants.RESOLUTION);
            resolution.setItems(new ListDataProvider<>(resolutions));
            resolutionVersion.setLabel(Constants.RESOLUTION_VERSION);
            horizontalLayout16.add(resolution,resolutionVersion);
            add(horizontalLayout16);

            HorizontalLayout horizontalLayout13 = new HorizontalLayout();
            horizontalLayout13.setWidthFull();
            horizontalLayout13.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            resolvedByField.setWidth(Constants.NAME_FIELD_WIDTH);
            resolvedByField.setLabel(Constants.RESOLVED_BY);
            resolvedByField.setItems(new ListDataProvider<>(employees));
            horizontalLayout13.add(resolvedByField,resolvedDatePicker);
            add(horizontalLayout13);

            HorizontalLayout horizontalLayout14 = new HorizontalLayout();
            horizontalLayout14.setWidthFull();
            horizontalLayout14.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            testedByField.setLabel(Constants.TESTED_BY);
            testedByField.setItems(new ListDataProvider<>(employees));
            testedByField.setWidth(Constants.NAME_FIELD_WIDTH);
            horizontalLayout14.add(testedByField,testedDatePicker);
            add(horizontalLayout14);

            HorizontalLayout horizontalLayout15 = new HorizontalLayout();
            horizontalLayout15.setWidthFull();
            horizontalLayout15.setAlignItems(FlexComponent.Alignment.START);
            horizontalLayout15.add(deferred);
            add(horizontalLayout15);

            if(userRole == Role.USER){
                programField.setReadOnly(true);
                releaseField.setReadOnly(true);
                versionField.setReadOnly(true);
                reportType.setReadOnly(true);
                severity.setReadOnly(true);
            }else if (userRole == Role.EMPLOYEE){
                programField.setReadOnly(true);
                releaseField.setReadOnly(true);
                versionField.setReadOnly(true);
                reportType.setReadOnly(true);
                priority.setReadOnly(true);
                assignedToField.setReadOnly(true);
            }else if(userRole == Role.MANAGER){
                programField.setReadOnly(true);
                releaseField.setReadOnly(true);
                versionField.setReadOnly(true);
                reportType.setReadOnly(true);
            }
        }else {
            if (userRole == Role.EMPLOYEE){
                priority.setReadOnly(true);
                assignedToField.setReadOnly(true);
            }
        }

        programField.addValueChangeListener(event ->{
            List<String> releaseList = new ArrayList<>(programData.get(programField.getValue()).keySet());
            Collections.sort(releaseList);
            releaseField.setItems(new ListDataProvider<>(releaseList));
            if (initial || userRole == Role.ADMIN) {
                releaseField.setReadOnly(false);
            }
        });
        releaseField.addValueChangeListener(event ->{
            List<String> versionsList = new ArrayList<>(programData.get(programField.getValue()).get(releaseField.getValue()).keySet());
            Collections.sort(versionsList);
            versionField.setItems(new ListDataProvider<>(versionsList));
            if (initial || userRole == Role.ADMIN) {
                versionField.setReadOnly(false);
            }
        });

    }

    public BugData getBugData(){
        BugData bugData = new BugData();
        bugData.setBugReportId(problemNumberField.getValue());
        bugData.setProgramName(programField.getValue());
        bugData.setRelease(releaseField.getValue());
        bugData.setVersion(versionField.getValue());
        bugData.setProgramId(programData.get(programField.getValue()).get(releaseField.getValue()).get(versionField.getValue()));
        bugData.setReportType(reportType.getValue());
        bugData.setSeverity(severity.getValue());
        bugData.setAttachments(attachments.getValue());
        bugData.setAttachmentDesc(ifYesDescribeField.getValue());
        bugData.setProblemSummary(problemSummaryField.getValue());
        bugData.setReproducible(reproducibleCheckbox.getValue());
        bugData.setProblemDescription(problemDescAndReproduceField.getValue());
        bugData.setSuggestedFix(suggestedFixField.getValue());
        bugData.setReportedBy(reportedByField.getValue());
        bugData.setReportedDate(java.sql.Date.valueOf(reportedDateField.getValue()));
        bugData.setFunctionalArea(functionalAreaField.getValue());
        bugData.setAssignedTo(assignedToField.getValue());
        bugData.setComments(commentsField.getValue());
        bugData.setStatus(status.getValue());
        bugData.setPriority(priority.getValue());
        bugData.setResolution(resolution.getValue());
        bugData.setResolutionVersion(resolutionVersion.getValue());
        bugData.setResolvedBy(resolvedByField.getValue());
        bugData.setResolvedDate(Validator.nullOrDate(resolvedDatePicker.getValue()));
        bugData.setTestedBy(testedByField.getValue());
        bugData.setTestedDate(Validator.nullOrDate(testedDatePicker.getValue()));
        bugData.setTreatAsDeferred(deferred.getValue());
        return bugData;
    }

    public void updateForm(BugData bugData){
        problemNumberField.setValue(bugData.getBugReportId());
        programField.setValue(bugData.getProgramName());
        releaseField.setValue(bugData.getRelease());
        versionField.setValue(bugData.getVersion());
        reportType.setValue(bugData.getReportType().getReportType());
        severity.setValue(bugData.getSeverity().getSeverity());
        attachments.setValue(bugData.isAttachments());
        ifYesDescribeField.setValue(Validator.emptyIfNull(bugData.getAttachmentDesc()));
        problemSummaryField.setValue(bugData.getProblemSummary());
        reproducibleCheckbox.setValue(bugData.getReproducible());
        problemDescAndReproduceField.setValue(bugData.getProblemDescription());
        suggestedFixField.setValue(Validator.emptyIfNull(bugData.getSuggestedFix()));
        reportedByField.setValue(bugData.getReportedBy());
        reportedDateField.setValue(bugData.getReportedDate().toLocalDate());
        functionalAreaField.setValue(Validator.emptyIfNull(bugData.getFunctionalArea()));
        assignedToField.setValue(Validator.nullOrString(bugData.getAssignedTo()));
        commentsField.setValue(Validator.emptyIfNull(bugData.getComments()));
        status.setValue(Validator.emptyIfNull(bugData.getStatus().getStatus()));
        priority.setValue(bugData.getPriority());
        resolution.setValue(Validator.emptyIfNull(bugData.getResolution().getResolution()));
        resolutionVersion.setValue(Validator.emptyIfNull(bugData.getResolutionVersion()));
        resolvedByField.setValue(Validator.nullOrString(bugData.getResolvedBy()));
        if(bugData.getResolvedDate() != null)
            resolvedDatePicker.setValue(bugData.getResolvedDate().toLocalDate());
        testedByField.setValue(Validator.nullOrString(bugData.getTestedBy()));
        if(bugData.getTestedDate() != null)
            testedDatePicker.setValue(bugData.getTestedDate().toLocalDate());
        deferred.setValue(bugData.getTreatAsDeferred());

        attachmentUpload.clearFileList();
    }

    public void configureUpload(){
        attachmentUpload.setAcceptedFileTypes("image/jpeg", "image/png", "application/pdf", "application/txt");
        attachmentUpload.setMaxFileSize(10*1024*1024);

        attachmentUpload.addFileRejectedListener(event->{
            String errorMessage = event.getErrorMessage();
            Notification.show(errorMessage,5000, Notification.Position.MIDDLE);
        });

    }

    public MultiFileBuffer getMultiFileBuffer(){
        if(attachments.getValue() && initial){
            if(multiFileBuffer.getFiles().isEmpty()){
                Notification.show("Please select a file to upload",5000, Notification.Position.MIDDLE);
                throw new IllegalStateException("No file selected");
            }
        }
        return multiFileBuffer;
    }
    public void setUploadList(List<String> list){
        uploadedAttachments.setItems(new ListDataProvider<>(list));
    }
    public Select<String> getUploadAttachmentSelect(){
        return uploadedAttachments;
    }
    public Button getDownloadAttachmentsButton(){
        return downloadAttachments;
    }
    public String getBugReportId(){
        return problemNumberField.getValue().toString();
    }
    public boolean hasAttachments(){
        return attachments.getValue();
    }
    public boolean isInitialSubmission(){
        return initial;
    }
}
