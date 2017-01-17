package com.prgpr.data;

import com.prgpr.Metadata.Scraping.PersonMetadataScraper;
import com.prgpr.framework.database.*;

import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Person Class.
 * It represents a Person, sourcing from a Wikipedia Page.
 *
 * @author Elizaveta Kovalevskaya
 */
public class Person extends EntityBase {

    private static final Logger log = LogManager.getFormatterLogger(Person.class);
    private static final String indexName = "Persons";

    public enum PersonAttribute implements Property {
        RAW_NAME,
        FIRST_NAME,
        LAST_NAME,
        BIRTH_NAME,
        DATE_OF_BIRTH,
        DATE_OF_DEATH,
        PLACE_OF_BIRTH,
        PLACE_OF_DEATH
    }

    public Person(Element node) {
        this.node = node;
        Set<Element> sources = SearchProvider.findImmediateOutgoing(node, RelationshipTypes.sourceLink);
        assert !sources.isEmpty();
        this.source = (Element) sources.toArray()[0];
    }

    public Person(EmbeddedDatabase db, Page page) {
        this.ownNamespaceID = 16; // As wiki namespaces hasn't got the namespaceid 16, lets take this.
        this.node = db.createElement(indexName, hashCode(page.getTitle(), ownNamespaceID), (node) -> {
            node.addLabel(EntityTypes.Person);
            node.setProperty(EntityAttribute.title, page.getTitle());
            node.setProperty(EntityAttribute.ownNamespaceID, ownNamespaceID);
        });
        this.source = SearchProvider.findNode(db, EntityTypes.Page, Page.PageAttribute.hash, page.getHashCode());
        insertSourceLink();
    }

    /**
     * Compares Person object to another Object
     *
     * @param o other Object to compare to
     * @return if the objects are equal as a bool
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (getHashCode() != person.getHashCode()) return false;
        return getTitle() != null ? getTitle().equals(person.getTitle()) : person.getTitle() == null;
    }

    public void extractMetadata() {
        HashMap<PersonAttribute, String> metadata = PersonMetadataScraper.scrapeMetadataTag(this);

        for (Person.PersonAttribute attribute : Person.PersonAttribute.values()) {
            this.node.setProperty(attribute, "");
        }

        for (Person.PersonAttribute attribute : metadata.keySet()) {
            this.node.setProperty(attribute, metadata.get(attribute));
        }
    }

    public String getRawName() {
        return (String)this.node.getProperty(PersonAttribute.RAW_NAME);
    }

    public String getLastName() {
        return (String)this.node.getProperty(PersonAttribute.LAST_NAME);
    }

    public String getFirstName() {
        return (String)this.node.getProperty(PersonAttribute.FIRST_NAME);
    }

    public String getBirthName() {
        return (String)this.node.getProperty(PersonAttribute.BIRTH_NAME);
    }

    public String getDateOfBirth() {
        return (String)this.node.getProperty(PersonAttribute.DATE_OF_BIRTH);
    }

    public String getDateOfDeath() {
        return (String)this.node.getProperty(PersonAttribute.DATE_OF_DEATH);
    }

    public String getPlaceOfBirth() {
        return (String)this.node.getProperty(PersonAttribute.PLACE_OF_BIRTH);
    }

    public String getPlaceOfDeath() {
        return (String)this.node.getProperty(PersonAttribute.PLACE_OF_DEATH);
    }

}