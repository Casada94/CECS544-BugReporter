package com.cecs544.BugReporter.util;

import com.cecs544.BugReporter.model.FunctionalAreaMapping;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
public class XMLService {

    public File generateXML(List<FunctionalAreaMapping> mappings) {
        if (mappings == null || mappings.isEmpty()) {
            return null;
        }
        String xmlString = generateXmlString(mappings);

        if (xmlString == null) {
            return null;
        }

        try {
            File file = new File("functional_areas.xml");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(xmlString);
            fileWriter.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateXmlString(List<FunctionalAreaMapping> mappings){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("FunctionalAreas");
            doc.appendChild(root);

            for (FunctionalAreaMapping mapping : mappings) {
                Element program = doc.createElement("Program");
                program.setAttribute("id", String.valueOf(mapping.getProgramId()));
                program.setAttribute("area", mapping.getFunctionalArea());
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


}
