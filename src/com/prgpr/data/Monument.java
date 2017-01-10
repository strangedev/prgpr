package com.prgpr.data;

import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;

/**
 * The Monument Class.
 * It represents a Monument, sourcing from a Wikipedia Page.
 *
 * @author Elizaveta Kovalevskaya
 */
public class Monument extends EntityBase {

    private static final Logger log = LogManager.getFormatterLogger(Monument.class);
    private static final String indexName = "Monuments";
    private static final int ownNamespaceID = 18; // As wiki namespaces hasn't got the namespaceid 18, lets take this.
    private Element node;

    public enum MonumentAttribute implements Property {
        hash,
        title
    }

    public Monument(Element node) {
        this.node = node;
    }

    public Monument(EmbeddedDatabase db, Page page) {
        this.node = db.createElement(indexName, hashCode(page.getTitle()), (node) -> {
            node.addLabel(EntityTypes.Monument);
            node.setProperty(Monument.MonumentAttribute.title, page.getTitle());
        });
        insertSourceLink(page);
    }

    /**
     * @return the hashcode of the Monument
     */
    public long getHashCode() { return (long)node.getProperty(Monument.MonumentAttribute.hash); }

    /**
     * @return the title of the Monument's article from Wikipedia (the name of the Monument)
     */
    public String getTitle() { return (String)node.getProperty(Monument.MonumentAttribute.title); }

    /**
     * @return the source of the Monument
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
     * Compares Monument object to another Object
     *
     * @param o other Object to compare to
     * @return if the objects are equal as a bool
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Monument monument = (Monument) o;

        if (getHashCode() != monument.getHashCode()) return false;
        return getTitle() != null ? getTitle().equals(monument.getTitle()) : monument.getTitle() == null;
    }

    /**
     * Inserts the sourceLink from the Monument to his Page by calling the super class method.
     *
     * @param page Source of the Wikidata
     * @return Title of the source
     */
    public String insertSourceLink(Page page) { return super.insertSourceLink(node, page);}

    /**
     * Inserts the EntityLinks from the Monument to his Pages Persons, Cities and Monuments.
     *
     * @return the Titles of the linking Persons, Cities and Monuments.
     */
    public Stream<String> insertEntityLinks() {
        return super.insertEntityLinks(node, getSource());
    }


}
