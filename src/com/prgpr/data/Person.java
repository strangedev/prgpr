package com.prgpr.data;

import com.prgpr.framework.database.*;

import java.util.stream.Stream;

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
    private static final int ownNamespaceID = 16; // As wiki namespaces hasn't got the namespaceid 16, lets take this.
    private Element node;

    public enum PersonAttribute implements Property {
        hash,
        title
    }

    public Person(Element node) {
        this.node = node;
    }

    public Person(EmbeddedDatabase db, Page page) {
        this.node = db.createElement(indexName, hashCode(page.getTitle()), (node) -> {
            node.addLabel(EntityTypes.Person);
            node.setProperty(PersonAttribute.title, page.getTitle());
        });
        insertSourceLink(page);
    }

    /**
     * @return the hashcode of the Person
     */
    public long getHashCode() { return (long)node.getProperty(PersonAttribute.hash); }

    /**
     * @return the title of the Person's article from Wikipedia (the name of the Person)
     */
    public String getTitle() { return (String)node.getProperty(PersonAttribute.title); }

    /**
     * @return the source of the Person
     */
    public Page getSource() {
        return super.getSource(this.node);
    }

    @Override
    public int hashCode() { return super.hashCode(getTitle(), ownNamespaceID); }

    /**
     * Calculates the hash.
     *
     * @param title of the page
     * @return the hash
     */
    private int hashCode(String title) { return super.hashCode(title, ownNamespaceID);}

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

    /**
     * Inserts the sourceLink from the Person to his Page by calling the super class method.
     *
     * @param page Source of the Wikidata
     * @return Title of the source
     */
    public String insertSourceLink(Page page) { return super.insertSourceLink(node, page);}

    /**
     * Inserts the EntityLinks from the Person to his Pages Persons, Cities and Monuments.
     *
     * @return the Titles of the linking Persons, Cities and Monuments.
     */
    public Stream<String> insertEntityLinks() {
        return super.insertEntityLinks(node, getSource());
        }


}