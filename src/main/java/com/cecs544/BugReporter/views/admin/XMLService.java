package com.cecs544.BugReporter.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

public class XMLService extends VerticalLayout {

    private String dbUrl = "jdbc:mysql://localhost:3306/CECS544-BugReporter";
    private String dbUser = "user";
    private String dbPassword = "password";

    public XMLService() {
        setupDownloadButton();
    }

    private void setupDownloadButton() {
        Button downloadButton = new Button("Download XML", new Icon(VaadinIcon.DOWNLOAD));
        downloadButton.addClickListener(event -> {
            List<FunctionalAreaMapping> data = fetchFunctionalAreaMappings();
            String xmlContent = generateXML(data);
            StreamResource resource = new StreamResource("functional_areas.xml",
                    () -> new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
            downloadButton.getElement().setAttribute("href", resource);
            downloadButton.getElement().setAttribute("download", true);
        });
        add(downloadButton);
    }

    private List<FunctionalAreaMapping> fetchFunctionalAreaMappings() {
        List<FunctionalAreaMapping> mappings = new ArrayList<>();
        String query = "SELECT PROGRAM_ID, FUNCTIONAL_AREA FROM FUNCTIONAL_AREA_MAPPING";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                mappings.add(new FunctionalAreaMapping(rs.getInt("PROGRAM_ID"), rs.getString("FUNCTIONAL_AREA")));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

    private static class FunctionalAreaMapping {
        int programId;
        String functionalArea;

        public FunctionalAreaMapping(int programId, String functionalArea) {
            this.programId = programId;
            this.functionalArea = functionalArea;
        }
    }
}
