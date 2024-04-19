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
import com.vaadin.flow.server.StreamResource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramTab extends Tab {
    private AdminView parent;

    private Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
    private Button exportButton = new Button(new Icon(VaadinIcon.DOWNLOAD_ALT));

    private List<Program> programs;
    private Grid<Program> grid = new Grid<>(Program.class);

    private TabSheet tabSheet = new TabSheet();

    private TextField programName = new TextField("Program Name");
    private VerticalLayout releaseVertLayout = new VerticalLayout();
    private List<ReleaseVersionFunctionField> rvfField = new ArrayList<>();
    private Button addRelease = new Button("Add Release");

    private TextField programNameUpdate = new TextField("Program Name");
    private VerticalLayout releaseVertLayoutUpdate = new VerticalLayout();
    private List<ReleaseVersionFunctionField> rvfFieldUpdate = new ArrayList<>();
    private Button addReleaseUpdate = new Button("Add Release");

    private Button addProgram = new Button("Add Program");
    private Button updateProgram = new Button("Update Program");

    public ProgramTab(AdminView parent, List<Program> program, List<String> roles) {
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
            ReleaseVersionFunctionField newRvfField = new ReleaseVersionFunctionField(this, rvfField.size());
            rvfField.add(newRvfField);
            releaseVertLayout.add(newRvfField);
        });
        addReleaseUpdate.addClickListener(click -> {
            ReleaseVersionFunctionField newRvfField = new ReleaseVersionFunctionField(this, rvfFieldUpdate.size());
            rvfFieldUpdate.add(newRvfField);
            releaseVertLayoutUpdate.add(newRvfField);
        });

        addProgram.addClickListener(e -> {
            Program tempProgram = new Program();
            tempProgram.setNAME(programName.getValue());
            tempProgram.setReleaseVersionFunctionMap(getReleaseFields());

            String errorMessage;
            if (!(errorMessage = Validator.validProgram(tempProgram)).isEmpty()) {
                Notification.show(errorMessage, 5000, Notification.Position.MIDDLE);
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
            if (!(errorMessage = Validator.validProgram(tempProgram)).isEmpty()) {
                Notification.show(errorMessage, 5000, Notification.Position.MIDDLE);
                return;
            }

            parent.updateProgram(tempProgram);
            programs.set(programs.indexOf(tempProgram), tempProgram);
            grid.setItems(programs);
        });

        exportButton.addClickListener(e -> {
            List<FunctionalAreaMapping> mappings = fetchFunctionalAreaMappings();
            String xmlContent = generateXML(mappings);
            if (xmlContent != null) {
                StreamResource resource = new StreamResource("functional_areas.xml",
                        () -> new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
                resource.setContentType("text/xml");
                resource.setCacheTime(0);
                getElement().executeJs("window.open($0, '_blank')", resource);
            }
        });

        releaseVertLayout.add(new ReleaseVersionFunctionField(this, 0));

        tabSheet.add("Add", new Tab(new HorizontalLayout(programName, releaseVertLayout)));
        HorizontalLayout expand = new HorizontalLayout(refreshButton, exportButton);
        expand.expand(refreshButton);
        expand.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(expand, grid, tabSheet);
    }

    public void setGridData(List<Program> program) {
        grid.setItems(program);
    }

    public Account getDataFromForm() {
        return null;
    }

    public void refreshForm() {
    }

    private Map<String, Map<String, List<String>>> getReleaseFields() {
        return rvfField.stream().collect(Collectors.toMap(ReleaseVersionFunctionField::getRelease, ReleaseVersionFunctionField::getVersionsFunctions));
    }

    private Map<String, Map<String, List<String>>> getReleaseUpdateFields() {
        return rvfFieldUpdate.stream().collect(Collectors.toMap(ReleaseVersionFunctionField::getRelease, ReleaseVersionFunctionField::getVersionsFunctions));
    }

    public void removeRelease(int index) {
        releaseVertLayout.remove(rvfField.get(index));
        rvfField.remove(index);
    }

    public void populateUpdateReleaseFields(Map<String, Map<String, List<String>>> releaseMap) {
        releaseVertLayoutUpdate.removeAll();
        rvfFieldUpdate.clear();
        for (String release : releaseMap.keySet()) {
            ReleaseVersionFunctionField newRvfField = new ReleaseVersionFunctionField(this, rvfFieldUpdate.size());
            newRvfField.addVersionFunctionFields(releaseMap.get(release));
            newRvfField.setRelease(release);
            rvfFieldUpdate.add(newRvfField);
            releaseVertLayoutUpdate.add(newRvfField);
        }
    }

    private List<FunctionalAreaMapping> fetchFunctionalAreaMappings() {
        List<FunctionalAreaMapping> mappings = new ArrayList<>();
        String query = "SELECT PROGRAM_ID, FUNCTIONAL_AREA FROM FUNCTIONAL_AREA_MAPPING";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "user", "password");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                mappings.add(new FunctionalAreaMapping(rs.getInt("PROGRAM_ID"), rs.getString("FUNCTIONAL_AREA")));
            }
        } catch (Exception e) {
            Notification.show("Database connection error: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
        return mappings;
    }

    private String generateXML(List<FunctionalAreaMapping> mappings) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("FunctionalAreas");
            doc.appendChild(root);

            for (FunctionalAreaMapping mapping : mappings) {
                Element program = doc.createElement("Program");
                program.setAttribute("id", String.valueOf(mapping.programId));
                program.setAttribute("area", mapping.functionalArea);
                root.appendChild(program);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(domSource, result);
            return writer.getBuffer().toString();
        } catch (Exception e) {
            Notification.show("Error generating XML: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            return null;
        }
    }

    private static class FunctionalAreaMapping {
        int programId;
        String functionalArea;

        FunctionalAreaMapping(int programId, String functionalArea) {
            this.programId = programId;
            this.functionalArea = functionalArea;
        }
    }
}
