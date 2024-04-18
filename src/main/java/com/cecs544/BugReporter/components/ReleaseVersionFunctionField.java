package com.cecs544.BugReporter.components;

import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.views.admin.ProgramTab;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReleaseVersionFunctionField extends HorizontalLayout {

    private ProgramTab parentLayout;
    private int index;
    private Button removeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
    private TextField release = new TextField(Constants.RELEASE);
    private List<VersionFunctionField> versionFunctionFields = new ArrayList<>();
    private VerticalLayout versionFunctionLayout = new VerticalLayout();
    private Button addButton = new Button("Add Version");

    public ReleaseVersionFunctionField(ProgramTab parent,int index) {
        parentLayout = parent;
        this.index = index;
        add(release);
        VersionFunctionField versionFunctionField = new VersionFunctionField(this,0);
        versionFunctionLayout.add(versionFunctionField);
        add(addButton);

        addButton.addClickListener(click -> {
            VersionFunctionField newVersionFunctionField = new VersionFunctionField(this,versionFunctionFields.size());
            versionFunctionFields.add(newVersionFunctionField);
            versionFunctionLayout.add(newVersionFunctionField);
        });
        removeButton.addClickListener(click -> {
            for(VersionFunctionField versionFunctionField1 : versionFunctionFields) {
                versionFunctionLayout.remove(versionFunctionField1);
            }
            parentLayout.removeRelease(this.index);
        });
    }

    public String getRelease() {
        return release.getValue();
    }
    public void setRelease(String release) {
        this.release.setValue(release);
    }
    public Map<String, List<String>> getVersionsFunctions() {
        return versionFunctionFields.stream().collect(
            Collectors.toMap(VersionFunctionField::getVersion, VersionFunctionField::getFunctions));
    }
    public void removeVersion(int index) {
        versionFunctionLayout.remove(versionFunctionFields.get(index));
        versionFunctionFields.remove(index);

    }
    public void addVersionFunctionFields(Map<String,List<String>> versionFunctionMap){
        for(String version: versionFunctionMap.keySet()){
            VersionFunctionField versionFunctionField = new VersionFunctionField(this,versionFunctionFields.size());
            versionFunctionField.setVersion(version);
            versionFunctionField.setFunctionalAreas(versionFunctionMap.get(version));
            versionFunctionFields.add(versionFunctionField);
            versionFunctionLayout.add(versionFunctionField);
        }
    }
}
