package com.cecs544.BugReporter.views.admin;

import com.cecs544.BugReporter.model.Account;
import com.cecs544.BugReporter.util.Constants;
import com.cecs544.BugReporter.util.Validator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class AccountsTab extends VerticalLayout {
    private AdminView parent;

    private Grid<Account> grid = new Grid<>(Account.class);

    private TabSheet tabSheet = new TabSheet();
    private Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
    private Button exportButton = new Button(new Icon(VaadinIcon.DOWNLOAD_ALT));
    private TextField username = new TextField("Username");
    private Select<String> role = new Select();
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");

    private TextField usernameUpdate = new TextField("Username");
    private Select<String> roleUpdate = new Select();
    private TextField firstNameUpdate = new TextField("First Name");
    private TextField lastNameUpdate = new TextField("Last Name");
    private Checkbox updatePassword = new Checkbox("Update Password");
    private Checkbox enabled = new Checkbox("Enabled");
    private Button addAccount = new Button("Add Account");
    private Button updateAccount = new Button("Update Account");
    private Button deleteAccount = new Button("Delete Account");
    private TextField confirmField = new TextField("confirm username");
    private Button confirmDelete = new Button("Confirm Delete");
    private List<Account> accounts;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AccountsTab(AdminView parent, List<Account> account,List<String> roles) {
        this.parent = parent;
        this.accounts = account;

        refreshButton.addClickListener(click -> {
            accounts = parent.getAccounts();
            if (tabSheet.getSelectedTab().getLabel().equals("Update")) {
                refreshForm();
            }
            grid.setItems(new ListDataProvider<>(accounts));
        });

        grid.addClassName("BugData");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setHeight("500px");
        grid.setColumns("AUTHORITY","USERNAME","FIRST_NAME","LAST_NAME","ENABLED","PASSWORD_CHANGE_REQUIRED");
        grid.setWidthFull();
        grid.setItems(accounts);
        grid.addItemClickListener(item -> {
            Account clickedAccount = item.getItem();
            usernameUpdate.setValue(clickedAccount.getUSERNAME());
            roleUpdate.setValue(clickedAccount.getAUTHORITY());
            firstNameUpdate.setValue(clickedAccount.getFIRST_NAME());
            lastNameUpdate.setValue(clickedAccount.getLAST_NAME());
            enabled.setValue(clickedAccount.getENABLED());
            updatePassword.setValue(clickedAccount.getPASSWORD_CHANGE_REQUIRED());
            confirmDelete.setVisible(false);
            confirmField.setVisible(false);
        });

        role.setLabel("Role");
        role.setItems(roles);
        roleUpdate.setLabel("Role");
        roleUpdate.setItems(roles);

        addAccount.addClickListener(e -> {
            Account tempAccount = new Account();
            tempAccount.setUSERNAME(username.getValue());
            tempAccount.setAUTHORITY(role.getValue());
            tempAccount.setFIRST_NAME(firstName.getValue());
            tempAccount.setLAST_NAME(lastName.getValue());

            String errorMessage;
            if(!(errorMessage=Validator.validAccount(tempAccount)).isEmpty()) {
                Notification.show(errorMessage,5000, Notification.Position.MIDDLE);
                return;
            }
            tempAccount.setPASSWORD(Constants.BCRYPT + passwordEncoder.encode(username.getValue()));
            tempAccount.setENABLED(true);
            tempAccount.setPASSWORD_CHANGE_REQUIRED(true);
            parent.addAccount(tempAccount);
            accounts.add(tempAccount);
            grid.setItems(accounts);
            refreshForm();
        });

        updateAccount.addClickListener(e -> {
            Account tempAccount = new Account();
            tempAccount.setUSERNAME(usernameUpdate.getValue());
            tempAccount.setAUTHORITY(roleUpdate.getValue());
            tempAccount.setFIRST_NAME(firstNameUpdate.getValue());
            tempAccount.setLAST_NAME(lastNameUpdate.getValue());

            String errorMessage;
            if(!(errorMessage=Validator.validAccount(tempAccount)).isEmpty()) {
                Notification.show(errorMessage,5000, Notification.Position.MIDDLE);
                return;
            }
            tempAccount.setENABLED(enabled.getValue());
            tempAccount.setPASSWORD_CHANGE_REQUIRED(updatePassword.getValue());
            parent.updateAccount(tempAccount);
            accounts.set(accounts.indexOf(tempAccount),tempAccount);
            grid.setItems(accounts);
        });
        deleteAccount.addClickListener(e -> {
            confirmDelete.setVisible(true);
            confirmField.setVisible(true);

        });
        confirmDelete.addClickListener(e -> {
            if(confirmField.getValue() != null && confirmField.getValue().equals(usernameUpdate.getValue())) {
                confirmDelete.setVisible(false);
                confirmField.setVisible(false);

                Account tempAccount = new Account();
                tempAccount.setUSERNAME(usernameUpdate.getValue());
                parent.deleteAccount(tempAccount);
                accounts.remove(tempAccount);
                grid.setItems(accounts);
                refreshForm();
            } else {
                Notification.show("Username does not match",5000, Notification.Position.MIDDLE);
            }
        });

        exportButton.addClickListener(e -> {
            parent.exportUsers();
        });

        VerticalLayout add = new VerticalLayout();
        add.add(new HorizontalLayout(username,firstName),new HorizontalLayout(role,lastName),addAccount);
        add.setAlignItems(FlexComponent.Alignment.CENTER);
        tabSheet.add("Add",new Tab(add));

        VerticalLayout update = new VerticalLayout();
        usernameUpdate.setReadOnly(true);
        confirmField.setVisible(false);
        confirmDelete.setVisible(false);
        HorizontalLayout deleteLayout = new HorizontalLayout(deleteAccount, confirmField,confirmDelete);
        deleteLayout.setAlignItems(Alignment.BASELINE);
        update.add(new HorizontalLayout(usernameUpdate,firstNameUpdate),new HorizontalLayout(roleUpdate,lastNameUpdate),updatePassword,enabled,updateAccount,deleteLayout);
        update.setAlignItems(FlexComponent.Alignment.CENTER);
        tabSheet.add("Update",new Tab(update));

        HorizontalLayout expand = new HorizontalLayout(refreshButton,exportButton);
        expand.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        expand.setWidthFull();
        add(expand,grid,tabSheet);

        setWidthFull();
        setAlignItems(FlexComponent.Alignment.CENTER);

    }


    private void refreshForm() {
        usernameUpdate.setValue("");
        roleUpdate.setValue("");
        firstNameUpdate.setValue("");
        lastNameUpdate.setValue("");
        enabled.setValue(false);
        updatePassword.setValue(false);
        confirmDelete.setVisible(false);
        confirmField.setVisible(false);
    }

}
