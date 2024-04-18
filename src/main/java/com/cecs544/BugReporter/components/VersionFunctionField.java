package com.cecs544.BugReporter.components;

import com.cecs544.BugReporter.util.Constants;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

public class VersionFunctionField extends HorizontalLayout {
    private ReleaseVersionFunctionField parentLayout;
    private int index;

    private Button removeButton = new Button("Remove");
    private TextField version = new TextField(Constants.VERSION);
    private List<FunctionalArea> functionFields = new ArrayList<>();
    private VerticalLayout functionLayout = new VerticalLayout();
    private Button addButton = new Button("Add Function");

    public VersionFunctionField(ReleaseVersionFunctionField parent, int index) {
        this.parentLayout = parent;
        this.index = index;

        add(version);
        FunctionalArea functionField = new FunctionalArea(this,0);
        functionFields.add(functionField);
        functionLayout.add(functionField);
        add(addButton);

        addButton.addClickListener(click -> {
            FunctionalArea newFunctionField = new FunctionalArea(this,functionFields.size());
            functionFields.add(newFunctionField);
            functionLayout.add(newFunctionField);
        });
        removeButton.addClickListener(click -> {
            for(FunctionalArea functionField1 : functionFields) {
                functionLayout.remove(functionField1);
            }
            parentLayout.removeVersion(this.index);
        });
    }

    public String getVersion() {
        return version.getValue();
    }
    public void setVersion(String version) {
        this.version.setValue(version);
    }

    public List<String> getFunctions() {
        List<String> functionValues = new ArrayList<>();
        for(FunctionalArea field : functionFields) {
            if(!field.getFunctionArea().isEmpty() && field.getFunctionArea().isBlank() && !functionValues.contains(field.getFunctionArea()))
                functionValues.add(field.getFunctionArea());
        }
        return functionValues;
    }

    public void removeFunctionalArea(int index) {
        functionLayout.remove(functionFields.get(index));
        functionFields.remove(index);
    }

    public void setFunctionalAreas(List<String> functionalAreas) {
        for(String area : functionalAreas) {
            FunctionalArea newFunctionField = new FunctionalArea(this,functionFields.size());
            newFunctionField.setFunctionField(area);
            functionFields.add(newFunctionField);
            functionLayout.add(newFunctionField);
        }
    }
}
