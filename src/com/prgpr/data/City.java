package com.prgpr.data;

import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;

/**
 * The City Class.
 * It represents a City, sourcing from a Wikipedia Page.
 *
 * @author Elizaveta Kovalevskaya
 */
public class City extends EntityBase {

    private static final Logger log = LogManager.getFormatterLogger(City.class);
    private static final String indexName = "Cities";
    private static final int ownNamespaceID = 17; // As wiki namespaces hasn't got the namespaceid 17, lets take this.
    private Element node;

    public enum CityAttribute implements Property {
        hash,
        title
    }

    public City(Element node) {
        this.node = node;
    }

    public City(EmbeddedDatabase db, Page page) {
        this.node = db.createElement(indexName, hashCode(page.getTitle()), (node) -> {
            node.addLabel(EntityTypes.City);
            node.setProperty(City.CityAttribute.title, page.getTitle());
        });
        insertSourceLink(page);
    }

    /**
     * @return the hashcode of the City
     */
    public long getHashCode() { return (long)node.getProperty(City.CityAttribute.hash); }

    /**
     * @return the title of the City's article from Wikipedia (the name of the City)
     */
    public String getTitle() { return (String)node.getProperty(City.CityAttribute.title); }

    /**
     * @return the source of the City
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
     * Compares City object to another Object
     *
     * @param o other Object to compare to
     * @return if the objects are equal as a bool
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (getHashCode() != city.getHashCode()) return false;
        return getTitle() != null ? getTitle().equals(city.getTitle()) : city.getTitle() == null;
    }

    /**
     * Inserts the sourceLink from the City to his Page by calling the super class method.
     *
     * @param page Source of the Wikidata
     * @return Title of the source
     */
    public String insertSourceLink(Page page) { return super.insertSourceLink(node, page);}

    /**
     * Inserts the EntityLinks from the City to his Pages Persons, Cities and Monuments.
     *
     * @return the Titles of the linking Persons, Cities and Monuments.
     */
    public Stream<String> insertEntityLinks() {
        return super.insertEntityLinks(node, getSource());
    }

}
