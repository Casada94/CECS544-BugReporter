package com.cecs544.BugReporter.components;

import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.views.admin.ProgramTab;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class FunctionalArea extends HorizontalLayout {
    private ProgramTab parentLayout;
    private int index;

    private Button removeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
    private TextField functionField = new TextField();

    public FunctionalArea(ProgramTab parent, int index) {
        this.parentLayout = parent;
        this.index = index;

        if(index == 0) {
            functionField.setLabel(Constants.FUNCTIONAL_AREA);
        }
        else {
            functionField.setLabel("");
        }

        setAlignItems(Alignment.BASELINE);
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
    public void updateIndex(int index) {
        if(index==0){
            functionField.setLabel(Constants.FUNCTIONAL_AREA);
        }
        this.index = index;
    }
}
