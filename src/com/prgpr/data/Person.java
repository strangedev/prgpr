package com.prgpr.data;

import com.prgpr.framework.database.*;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.parser.Entity;

/**
 * The Person Class.
 * It represents a Person, sourcing from a Wikipedia Page.
 *
 * @author Elizaveta Kovalevskaya
 */
public class Person extends EntityBase {

    private static final Logger log = LogManager.getFormatterLogger(Person.class);
    private static final String indexName = "Persons";

    public enum PersonAttribute implements Property { }

    public Person(Element node) {
        this.node = node;
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

}