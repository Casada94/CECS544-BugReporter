package com.cecs544.BugReporter.views.admin;

import com.cecs544.BugReporter.components.ReleaseVersionFunctionField;
import com.cecs544.BugReporter.model.Account;
import com.cecs544.BugReporter.model.Program;
import com.cecs544.BugReporter.util.Validator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramTab extends Tab {
    private AdminView parent;

    private Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
    private Button exportButton = new Button(new Icon(VaadinIcon.DOWNLOAD_ALT));

//    private Map<Integer,Program> programs;
    private List<Program> programs;
    private Grid<Program> grid = new Grid<>(Program.class);

    private TabSheet tabSheet = new TabSheet();


    /**  ADD TAB  **/
    private TextField programName = new TextField("Program Name");

    private VerticalLayout releaseVertLayout = new VerticalLayout();
    private List<ReleaseVersionFunctionField> rvfField = new ArrayList<>();
    private Button addRelease = new Button("Add Release");

    /**  UPDATE TAB  **/
    private TextField programNameUpdate = new TextField("Program Name");

    private VerticalLayout releaseVertLayoutUpdate = new VerticalLayout();
    private List<ReleaseVersionFunctionField> rvfFieldUpdate = new ArrayList<>();
    private Button addReleaseUpdate = new Button("Add Release");

    private Button addProgram = new Button("Add Program");
    private Button updateProgram = new Button("Update Program");



//    public ProgramTab(AdminView parent, Map<Integer,Program> program,List<String> roles){
    public ProgramTab(AdminView parent, List<Program> program,List<String> roles){
        this.parent = parent;
        this.programs = program;


        refreshButton.addClickListener(click -> {
            programs = new ArrayList<>(parent.getPrograms().values());
            if (tabSheet.getSelectedTab().getLabel().equals("Update")) {
                refreshForm();
            }
            grid.setItems(new ListDataProvider<>(programs));
        });


        grid.addClassName("ProgramData");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setItems(programs);

        grid.addItemClickListener(item -> {
            Program clickedProgram = item.getItem();
            programName.setValue(clickedProgram.getNAME());
            populateUpdateReleaseFields(clickedProgram.getReleaseVersionFunctionMap());
        });

        addRelease.addClickListener(click -> {
            ReleaseVersionFunctionField newRvfField = new ReleaseVersionFunctionField(this,rvfField.size());
            rvfField.add(newRvfField);
            releaseVertLayout.add(newRvfField);
        });
        addReleaseUpdate.addClickListener(click -> {
            ReleaseVersionFunctionField newRvfField = new ReleaseVersionFunctionField(this,rvfFieldUpdate.size());
            rvfFieldUpdate.add(newRvfField);
            releaseVertLayoutUpdate.add(newRvfField);
        });

        addProgram.addClickListener(e -> {
            Program tempProgram = new Program();
            tempProgram.setNAME(programName.getValue());
            tempProgram.setReleaseVersionFunctionMap(getReleaseFields());

            String errorMessage;
            if(!(errorMessage=Validator.validProgram(tempProgram)).isEmpty()) {
                Notification.show(errorMessage,5000, Notification.Position.MIDDLE);
                return;
            }

            parent.addProgram(tempProgram);
            programs.add(tempProgram);
            grid.setItems(programs);
        });

        updateProgram.addClickListener(e -> {
            Program tempProgram = new Program();
            tempProgram.setNAME(programNameUpdate.getValue());
            tempProgram.setReleaseVersionFunctionMap(getReleaseUpdateFields());

            String errorMessage;
            if(!(errorMessage=Validator.validProgram(tempProgram)).isEmpty()) {
                Notification.show(errorMessage,5000, Notification.Position.MIDDLE);
                return;
            }

            parent.updateProgram(tempProgram);
            programs.set(programs.indexOf(tempProgram),tempProgram);
            grid.setItems(programs);
        });

        exportButton.addClickListener(e -> {
            //TODO: Export to XML
        });

        releaseVertLayout.add(new ReleaseVersionFunctionField(this,0));

        tabSheet.add("Add",new Tab(new HorizontalLayout(programName,releaseVertLayout)));
//        tabSheet.add("Update",new Tab(new VerticalLayout(new HorizontalLayout(programNameUpdate,releaseVertLayoutUpdate),new HorizontalLayout(updateProgram,deleteProgram))));
        HorizontalLayout expand = new HorizontalLayout(refreshButton,exportButton);
        expand.expand(refreshButton);
        expand.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(expand,grid,tabSheet);

    }

    public void setGridData(List<Program> program) {
        grid.setItems(program);
    }
    public Account getDataFromForm() {
        return null;
    }
    public void refreshForm() {
    }
    private Map<String, Map<String,List<String>>> getReleaseFields() {
        return rvfField.stream().collect(
                Collectors.toMap(ReleaseVersionFunctionField::getRelease, ReleaseVersionFunctionField::getVersionsFunctions));
    }
    private Map<String, Map<String,List<String>>> getReleaseUpdateFields() {
        return rvfFieldUpdate.stream().collect(
                Collectors.toMap(ReleaseVersionFunctionField::getRelease, ReleaseVersionFunctionField::getVersionsFunctions));
    }
    public void removeRelease(int index) {
        releaseVertLayout.remove(rvfField.get(index));
        rvfField.remove(index);
    }

    public void populateUpdateReleaseFields(Map<String, Map<String,List<String>>> releaseMap) {
        releaseVertLayoutUpdate.removeAll();
        rvfFieldUpdate.clear();
        for(String release: releaseMap.keySet()) {
            ReleaseVersionFunctionField newRvfField = new ReleaseVersionFunctionField(this,rvfFieldUpdate.size());
            newRvfField.addVersionFunctionFields(releaseMap.get(release));
            newRvfField.setRelease(release);
            rvfFieldUpdate.add(newRvfField);
            releaseVertLayoutUpdate.add(newRvfField);
        }
    }
}
