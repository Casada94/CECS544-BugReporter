package com.cecs544.BugReporter.views.admin;

import com.cecs544.BugReporter.components.FunctionalArea;
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
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProgramTab extends VerticalLayout {
    private AdminView parent;

    private Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
    private Button exportButton = new Button(new Icon(VaadinIcon.DOWNLOAD_ALT));

    private List<Program> programs;
    private Grid<Program> grid = new Grid<>(Program.class);

    private TabSheet tabSheet = new TabSheet();

    /**  ADD TAB  **/
    private TextField programName = new TextField("Program Name");
    private TextField version = new TextField("Version");
    private TextField release = new TextField("Release");

    private VerticalLayout releaseVertLayout = new VerticalLayout();
    private List<FunctionalArea> rvfField = new ArrayList<>();
    private Button addFunctionalArea = new Button(new Icon(VaadinIcon.PLUS));

    private Button addProgram = new Button("Add Program");
    private Button clearAddForm = new Button("Clear");

    /**  UPDATE TAB  **/
    private TextField programNameUpdate = new TextField("Program Name");
    private TextField versionUpdate = new TextField("Version");
    private TextField releaseUpdate = new TextField("Release");

    private VerticalLayout releaseVertLayoutUpdate = new VerticalLayout();
    private List<FunctionalArea> rvfFieldUpdate = new ArrayList<>();
    private Button addFunctionalAreaUpdate = new Button(new Icon(VaadinIcon.PLUS));

    private Button updateProgram = new Button("Update Program");
    private Button deleteProgram = new Button("Delete Program");
    private Button clearUpdateForm = new Button("Clear");
    private Integer lastClickedId;

    public ProgramTab(AdminView parent, List<Program> program){
        this.parent = parent;
        this.programs = program;

        releaseUpdate.setReadOnly(true);
        versionUpdate.setReadOnly(true);

        refreshButton.addClickListener(click -> {
            programs = new ArrayList<>(parent.getPrograms());
            if (tabSheet.getSelectedTab().getLabel().equals("Update")) {
                refreshAddForm();
                refreshUpdateForm();
            }
            grid.setItems(new ListDataProvider<>(programs));
        });

        clearAddForm.addClickListener(click ->{
            refreshAddForm();
        });
        clearUpdateForm.addClickListener(click ->{
            refreshUpdateForm();
        });

        grid.addClassName("ProgramData");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setHeight("500px");
        grid.setWidthFull();
        grid.setItems(programs);

        grid.addItemClickListener(item -> {
            Program clickedProgram = item.getItem();
            lastClickedId = clickedProgram.getID();
            programNameUpdate.setValue(clickedProgram.getNAME());
            versionUpdate.setValue(clickedProgram.getVersion());
            releaseUpdate.setValue(clickedProgram.getRelease());
            rvfFieldUpdate.clear();
            releaseVertLayoutUpdate.removeAll();
            for(String function : clickedProgram.getFunction()) {
                FunctionalArea newRvfField = new FunctionalArea(this,rvfFieldUpdate.size());
                newRvfField.setFunctionField(function);
                rvfFieldUpdate.add(newRvfField);
                releaseVertLayoutUpdate.add(newRvfField);
            }
        });

        addFunctionalArea.addClickListener(click -> {
            FunctionalArea functionalArea = new FunctionalArea(this,rvfField.size());
            rvfField.add(functionalArea);
            releaseVertLayout.add(functionalArea);
        });
        addFunctionalAreaUpdate.addClickListener(click -> {
            FunctionalArea functionalArea = new FunctionalArea(this,rvfFieldUpdate.size());
            rvfFieldUpdate.add(functionalArea);
            releaseVertLayoutUpdate.add(functionalArea);
        });

        addProgram.addClickListener(e -> {
            Program tempProgram = new Program();
            tempProgram.setNAME(programName.getValue());
            tempProgram.setRelease(release.getValue());
            tempProgram.setVersion(version.getValue());
            tempProgram.setFunction(getFunctionalAreas());

            String errorMessage;
            if(!(errorMessage=Validator.validProgram(tempProgram)).isEmpty()) {
                Notification.show(errorMessage,5000, Notification.Position.MIDDLE);
                return;
            }

            try{
                parent.addProgram(tempProgram);
            } catch(DataAccessException dae){
                Notification.show("Error adding program",5000, Notification.Position.MIDDLE);
                return;
            }
            programs.add(tempProgram);
            grid.setItems(programs);

            rvfField.clear();
            releaseVertLayout.removeAll();
            programName.setValue("");
            release.setValue("");
            version.setValue("");
            FunctionalArea newRvfField = new FunctionalArea(this,rvfField.size());
            rvfField.add(newRvfField);
            releaseVertLayout.add(newRvfField);
            Notification.show("Successfully Added a New Porgram",5000, Notification.Position.MIDDLE);
        });

        updateProgram.addClickListener(e -> {
            Program tempProgram = new Program();
            tempProgram.setNAME(programNameUpdate.getValue());
            tempProgram.setRelease(releaseUpdate.getValue());
            tempProgram.setVersion(versionUpdate.getValue());
            tempProgram.setFunction(getUpdateFunctionalAreas());

            String errorMessage;
            if(!(errorMessage=Validator.validProgram(tempProgram)).isEmpty()) {
                Notification.show(errorMessage,5000, Notification.Position.MIDDLE);
                return;
            }
            Integer id = lastClickedId;
            if(id ==null){
                id = findProgramId(tempProgram);
                if(id ==null){
                    Notification.show("Program not found",5000, Notification.Position.MIDDLE);
                    return;
                }
            }
            tempProgram.setID(id);
            parent.updateProgram(tempProgram);

            Set<String> original = tempProgram.getFunction();
            if(tempProgram.getFunction().size() < programs.get(programs.indexOf(tempProgram)).getFunction().size()){
                Set<String> difference = new HashSet<>(programs.get(programs.indexOf(tempProgram)).getFunction());
                difference.removeAll(tempProgram.getFunction());
                tempProgram.setFunction(difference);
                parent.removeFunctionalAreas(tempProgram);
            }
            tempProgram.setFunction(original);
            programs.set(programs.indexOf(tempProgram),tempProgram);
            grid.setItems(programs);
            lastClickedId= null;
            Notification.show("Successfully Updated Program", 5000, Notification.Position.MIDDLE);
        });

        exportButton.addClickListener(e -> {
            parent.exportFunctionalAreas();
        });

        FunctionalArea rvff1 = new FunctionalArea(this,0);
        rvfField.add(rvff1);
        releaseVertLayout.add(rvff1);
        HorizontalLayout programNameLayout = new HorizontalLayout(new VerticalLayout(programName,release,version),new VerticalLayout(releaseVertLayout,addFunctionalArea));
        programNameLayout.setAlignItems(Alignment.BASELINE);
        tabSheet.add("Add",new VerticalLayout(programNameLayout,addProgram,clearAddForm));

        FunctionalArea rvff2 = new FunctionalArea(this,0);
        rvfFieldUpdate.add(rvff2);
        releaseVertLayoutUpdate.add(rvff2);
        HorizontalLayout programNameLayoutUpdate = new HorizontalLayout(new VerticalLayout(programNameUpdate,releaseUpdate,versionUpdate),new VerticalLayout(releaseVertLayoutUpdate,addFunctionalAreaUpdate));
        programNameLayoutUpdate.setAlignItems(Alignment.BASELINE);
        tabSheet.add("Update",new VerticalLayout(programNameLayoutUpdate,new HorizontalLayout(updateProgram,deleteProgram,clearUpdateForm)));
        HorizontalLayout expand = new HorizontalLayout(refreshButton,exportButton);
        expand.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        expand.setWidthFull();
        setWidthFull();
        add(expand,grid,tabSheet);
//        add(addProgram);
    }

    private Integer findProgramId(Program input){
        for(Program program : programs){
            if(input.equals(program)){
                return program.getID();
            }
        }
        return null;
    }

    public void refreshAddForm(){
        programName.setValue("");
        version.setValue("");
        release.setValue("");
        rvfField.clear();
        releaseVertLayout.removeAll();
        FunctionalArea newRvfField = new FunctionalArea(this,rvfField.size());
        rvfField.add(newRvfField);
        releaseVertLayout.add(newRvfField);

    }
    public void refreshUpdateForm() {
        programNameUpdate.setValue("");
        versionUpdate.setValue("");
        releaseUpdate.setValue("");
        rvfFieldUpdate.clear();
        releaseVertLayoutUpdate.removeAll();
        FunctionalArea newRvfFieldUpdate = new FunctionalArea(this,rvfFieldUpdate.size());
        rvfFieldUpdate.add(newRvfFieldUpdate);
        releaseVertLayoutUpdate.add(newRvfFieldUpdate);
    }

    public void removeRelease(int index) {
        if(tabSheet.getSelectedTab().getLabel().equals("Update")){
            releaseVertLayoutUpdate.remove(rvfFieldUpdate.get(index));
            rvfFieldUpdate.remove(index);
            for(int i = 0; i<rvfFieldUpdate.size();i++) {
                rvfFieldUpdate.get(i).updateIndex(i);
            }
        } else{
            releaseVertLayout.remove(rvfField.get(index));
            rvfField.remove(index);
            for(int i = 0; i<rvfField.size();i++) {
                rvfField.get(i).updateIndex(i);
            }
        }
    }

    public void removeFunctionalArea(int index){
        if(tabSheet.getSelectedTab().getLabel().equals("Update")){
            releaseVertLayoutUpdate.remove(rvfFieldUpdate.get(index));
            rvfFieldUpdate.remove(index);
            for(int i = 0; i<rvfFieldUpdate.size();i++) {
                rvfFieldUpdate.get(i).updateIndex(i);
            }
        } else{
            releaseVertLayout.remove(rvfField.get(index));
            rvfField.remove(index);
            for(int i = 0; i<rvfField.size();i++) {
                rvfField.get(i).updateIndex(i);
            }
        }
    }

    public Set<String> getFunctionalAreas(){
        Set<String> functionalAreas = new HashSet<>();
        for(FunctionalArea field : rvfField) {
            if(!field.getFunctionArea().isBlank())
                functionalAreas.add(field.getFunctionArea());
        }
        if(functionalAreas.isEmpty()) {
            return null;
        }
        return functionalAreas;
    }
    public Set<String> getUpdateFunctionalAreas(){
        Set<String> functionalAreas = new HashSet<>();
        for(FunctionalArea field : rvfFieldUpdate) {
            if(!field.getFunctionArea().isBlank())
                functionalAreas.add(field.getFunctionArea());
        }
        if(functionalAreas.isEmpty()) {
            return null;
        }
        return functionalAreas;
    }
}
