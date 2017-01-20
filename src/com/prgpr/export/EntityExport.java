package com.prgpr.export;

import com.prgpr.data.City;
import com.prgpr.data.EntityBase;
import com.prgpr.data.Monument;
import com.prgpr.data.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;


/**
 * Created by strange on 1/20/17.
 * @author Noah Hummel
 */
public class EntityExport {

    private static final Logger log = LogManager.getFormatterLogger(EntityExport.class);

    public static Element makeEntitiesRoot(SimpleXmlDocument document) {
        return document.createElement("entities");
    }

    public static void appendEntity(EntityBase entity, SimpleXmlDocument document, Element parent) {
        if (entity == null) return;

        Element entityRoot = makeEntityRoot(entity, document);
        parent.appendChild(entityRoot);

        if (entity instanceof Person) {
            addPersonAttributes((Person)entity, document, entityRoot);
        } else if (entity instanceof City) {
            addCityAttributes((City)entity, document, entityRoot);
        } else if (entity instanceof Monument) {
            addMonumentAttributes((Monument)entity, document, entityRoot);
        } else log.error("Entity type not supported, canceling export.");
    }

    private static Element makeEntityRoot(EntityBase entity, SimpleXmlDocument document) {
        Element entityRoot = document.createElement(entity.getClass().getName());
        entityRoot.setAttribute("id", entity.getId());
        entityRoot.setAttribute("sourceId", entity.getSource().getId());
        entityRoot.setAttribute("title", entity.getTitle());
        entityRoot.setAttribute("wikidataEntityId", entity.getEntityId());
        return entityRoot;
    }

    private static void addPersonAttributes(Person person, SimpleXmlDocument document, Element parent) {
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

    private static void addCityAttributes(City city, SimpleXmlDocument document, Element parent) {
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

    private static void addMonumentAttributes(Monument monument, SimpleXmlDocument document, Element parent) {
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
