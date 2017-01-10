package com.prgpr.data;

import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.Property;
import com.prgpr.framework.database.SearchProvider;
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

    public enum CityAttribute implements Property { }

    public City(Element node) {
        this.node = node;
    }

    public City(EmbeddedDatabase db, Page page) {
        this.ownNamespaceID = 17; // As wiki namespaces hasn't got the namespaceid 17, lets take this.
        this.node = db.createElement(indexName, hashCode(page.getTitle(), ownNamespaceID), (node) -> {
            node.addLabel(EntityTypes.City);
            node.setProperty(EntityAttribute.title, page.getTitle());
            node.setProperty(EntityAttribute.ownNamespaceID, ownNamespaceID);
        });
        this.source = SearchProvider.findNode(db, EntityTypes.Page, Page.PageAttribute.hash, page.getHashCode());
        insertSourceLink();
    }

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

}
