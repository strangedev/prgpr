package com.prgpr;

import com.prgpr.data.City;
import com.prgpr.data.EntityBase;
import com.prgpr.data.Monument;
import com.prgpr.data.Person;
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
import java.util.stream.Stream;

/**
 * Created by strange on 1/20/17.
 * @author Noah Hummel
 */
public class EntityExport {

    private static final Logger log = LogManager.getFormatterLogger(EntityExport.class);
    private static final int TUTORIAL_NUMBER = 1337;  // TODO
    private static final char GROUP_ID = 'X';  // TODO

    // TODO: refactor to be appender method
    public static String exportMany(Stream<EntityBase> entities) {

        Document document = prepareDocument();
        if (document == null) return "Bit of a fuck up here";

        Element root = buildXmlRoot(document);
        document.appendChild(root);

        entities.forEach(entity -> appendEntity(entity, document, root));

        return stringify(document);
    }

    public static String export(EntityBase entity) {

        Document document = prepareDocument();
        if (document == null) return "Bit of a fuck up here";

        Element root = buildXmlRoot(document);
        document.appendChild(root);

        appendEntity(entity, document, root);

        return stringify(document);
    }

    private static Document prepareDocument() {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        Document document;

        try {
            icBuilder = icFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("Could not create document builder" + e.getMessage());
            return null;
        }

        return icBuilder.newDocument();
    }

    private static String stringify(Document document) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // set utf8 encoding
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // set the indentation
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            return writer.getBuffer().toString();

        } catch (Exception e) {
            log.error("Couldn't write XML: " +  e.getMessage());
            return "http://wiki.c2.com/?XmlSucks";
        }
    }

    private static void appendEntity(EntityBase entity, Document document, Element parent) {
        if (entity == null) return;

        Element entityRoot = makeEntityRoot(entity, document);
        document.appendChild(entityRoot);

        if (entity instanceof Person) {
            addPersonAttributes((Person)entity, document, entityRoot);
        } else if (entity instanceof City) {
            addCityAttributes((City)entity, document, entityRoot);
        } else if (entity instanceof Monument) {
            addMonumentAttributes((Monument)entity, document, entityRoot);
        } else log.error("Entity type not supported, canceling export.");
    }

    private static Element buildXmlRoot(Document document) {
        Element xmlRoot = document.createElement("wikixtractor");
        xmlRoot.setAttribute("tutorium", String.valueOf(TUTORIAL_NUMBER));
        xmlRoot.setAttribute("group", String.valueOf(GROUP_ID));
        return xmlRoot;
    }

    private static Element makeEntityRoot(EntityBase entity, Document document) {
        Element entityRoot = document.createElement(entity.getClass().getName());
        entityRoot.setAttribute("id", entity.getId());
        entityRoot.setAttribute("sourceId", entity.getSource().getId());
        entityRoot.setAttribute("title", entity.getTitle());
        entityRoot.setAttribute("wikidataEntityId", entity.getEntityId());

        return entityRoot;
    }

    private static void addPersonAttributes(Person person, Document document, Element parent) {
        Element names = document.createElement("name");
        names.setAttribute("first", person.getFirstName());
        names.setAttribute("last", person.getLastName());
        names.setAttribute("birth", person.getBirthName());
        names.setAttribute("raw", person.getRawName());
        parent.appendChild(names);

        Element dates = document.createElement("birth");
        names.setAttribute("date", person.getDateOfBirth());
        names.setAttribute("place", person.getPlaceOfBirth());
        parent.appendChild(dates);

        Element places = document.createElement("death");
        names.setAttribute("date", person.getDateOfDeath());
        names.setAttribute("place", person.getPlaceOfDeath());
        parent.appendChild(places);
    }

    private static void addCityAttributes(City city, Document document, Element parent) {
        Element name = document.createElement("name");
        name.setAttribute("name", city.getName());
        parent.appendChild(name);

        Element population = document.createElement("population");
        name.setAttribute("amount", city.getPopulation());
        parent.appendChild(population);

        Element country = document.createElement("country");
        name.setAttribute("name", city.getCountry());
        parent.appendChild(country);

        Element earliestMention = document.createElement("earliestMention");
        name.setAttribute("date", city.getEarliestMention());
        parent.appendChild(earliestMention);
    }

    private static void addMonumentAttributes(Monument monument, Document document, Element parent) {
        Element name = document.createElement("name");
        name.setAttribute("name", monument.getName());
        parent.appendChild(name);

        Element creation = document.createElement("creation");
        name.setAttribute("date",monument.getCreationDate());
        parent.appendChild(creation);

        Element inauguration = document.createElement("inauguration");
        name.setAttribute("date", monument.getInaugurationDate());
        parent.appendChild(inauguration);

        Element nearestCity = document.createElement("nearestCity");
        City nearest = monument.getNearestCity();
        if (nearest != null) {
            name.setAttribute("name", nearest.getName());
            parent.appendChild(nearestCity);
        }

        Element commemorates = document.createElement("commemorates");
        Person dude = monument.getCommemoratedPerson();
        if (nearest != null) {
            name.setAttribute("name", dude.getFirstName() + dude.getLastName());
            parent.appendChild(commemorates);
        }

    }
}
