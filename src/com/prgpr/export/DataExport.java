package com.prgpr.export;

import com.prgpr.data.EntityBase;
import com.prgpr.data.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by strange on 1/20/17.
 * @author Noah Hummel
 */
public class DataExport {

    private static final Logger log = LogManager.getFormatterLogger(DataExport.class);
    private static final int TUTORIAL_NUMBER = 1337;  // TODO
    private static final char GROUP_ID = 'X';  // TODO

    public static void appendPage(Page page, SimpleXmlDocument document) {
        Element pagesRoot = PageExport.makePagesRoot(document);
        PageExport.appendPage(page, document, pagesRoot);
        document.append(pagesRoot);
    }

    public static void appendEntity(EntityBase entity, SimpleXmlDocument document) {
        Element entitiesRoot = EntityExport.makeEntitiesRoot(document);
        EntityExport.appendEntity(entity, document, entitiesRoot);
        document.append(entitiesRoot);
    }

    public static void appendPages(Set<Page> pages, SimpleXmlDocument document) {
        Element pagesRoot = PageExport.makePagesRoot(document);
        pages.forEach(page -> {
            PageExport.appendPage(page, document, pagesRoot);
        });
        document.append(pagesRoot);
    }

    public static void appendEntities(Set<EntityBase> entities, SimpleXmlDocument document) {
        Element entitiesRoot = EntityExport.makeEntitiesRoot(document);
        entities.forEach(entity -> {
            EntityExport.appendEntity(entity, document, entitiesRoot);
        });
        document.append(entitiesRoot);
    }

    public static void appendPageStream(Stream<Page> pages, SimpleXmlDocument document) {
        Element pagesRoot = PageExport.makePagesRoot(document);
        pages.forEach(page -> {
            PageExport.appendPage(page, document, pagesRoot);
        });
        document.append(pagesRoot);
    }

    public static void appendEntityStream(Stream<EntityBase> entities, SimpleXmlDocument document) {
        Element entitiesRoot = EntityExport.makeEntitiesRoot(document);
        entities.forEach(entity -> {
            EntityExport.appendEntity(entity, document, entitiesRoot);
        });
        document.append(entitiesRoot);
    }

    public static SimpleXmlDocument newDocument() {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        Document document;

        try {
            icBuilder = icFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("Could not create document builder" + e.getMessage());
            return null;
        }

        document = icBuilder.newDocument();

        Element xmlRoot = document.createElement("wikixtractor");
        xmlRoot.setAttribute("tutorium", String.valueOf(TUTORIAL_NUMBER));
        xmlRoot.setAttribute("group", String.valueOf(GROUP_ID));
        document.appendChild(xmlRoot);

        return new SimpleXmlDocument(document, xmlRoot);
    }

    public static String stringify(SimpleXmlDocument document) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // set utf8 encoding
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // set the indentation
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document.getDocument()), new StreamResult(writer));

            return writer.getBuffer().toString();

        } catch (Exception e) {
            log.error("Couldn't write XML: " +  e.getMessage());
            return "http://wiki.c2.com/?XmlSucks";
        }
    }

}
