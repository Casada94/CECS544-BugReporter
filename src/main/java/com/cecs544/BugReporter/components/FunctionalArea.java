package com.cecs544.BugReporter.components;

import com.cecs544.BugReporter.util.Constants;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class FunctionalArea extends HorizontalLayout {
    private VersionFunctionField parentLayout;
    private int index;

    private Button removeButton = new Button("Remove");
    private TextField functionField = new TextField();

    public FunctionalArea(VersionFunctionField parent, int index) {
        this.parentLayout = parent;
        this.index = index;

        if(index == 0) {
            functionField.setLabel(Constants.FUNCTIONAL_AREA);
        }
        else {
            functionField.setLabel("");
        }

        add(functionField);
        add(removeButton);

        removeButton.addClickListener(click -> {
            parentLayout.removeFunctionalArea(this.index);
        });
    }

    public String getFunctionArea() {
        return functionField.getValue();
    }
    public void setFunctionField(String functionField) {
        this.functionField.setValue(functionField);
    }
}
