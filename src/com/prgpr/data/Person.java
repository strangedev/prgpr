package com.prgpr.data;

import com.prgpr.framework.database.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Elizaveta Kovalevskaya
 *
 * A Class to create Person objects.
 */
public class Person extends EntityBase {

    private static final Logger log = LogManager.getFormatterLogger(Page.class);
    private static final String indexName = "Persons";
    private static final String stringHashFunction = "SHA-1";
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

    public long getHashCode() { return (long)node.getProperty(PersonAttribute.hash); }

    public String getTitle() { return (String)node.getProperty(PersonAttribute.title); }

    public Page getSource() {
        return SearchProvider.findImmediateOutgoing(
            this.node,
            RelationshipTypes.sourceLink
        )
            .stream()
            .map(Page::new)
            .iterator().next();
    }

    @Override
    public int hashCode() { return hashCode(getTitle()); }

    /**
     * Calculates the hash.
     *
     * http://eclipsesource.com/blogs/2012/09/04/the-3-things-you-should-know-about-hashcode/
     * http://preshing.com/20110504/hash-collision-probabilities/
     * http://stackoverflow.com/questions/1867191/probability-of-sha1-collisions
     *
     * @param title of the page
     * @return the hash
     */
    private static int hashCode(String title) {
        byte[] titleHash;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(stringHashFunction);
            messageDigest.update(title.getBytes());
            titleHash = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            titleHash = title.getBytes();
        }

        return (ownNamespaceID * 31) + Arrays.hashCode(titleHash);
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

    /**
     * Inserts the sourceLink from the Person to his Page.
     *
     * @param page Source of the Wikidata
     * @return Title of the source
     */
    public String insertSourceLink(Page page) {
        node.getDatabase().transaction(() -> {
            Element elem = node.getDatabase().getNodeFromIndex("Pages", page.getHashCode());
            if(elem != null) { node.createUniqueRelationshipTo(elem, RelationshipTypes.sourceLink); }
        });

        return page.getTitle();
    }

    //public Stream<String> insertEntityLinks() { }
}