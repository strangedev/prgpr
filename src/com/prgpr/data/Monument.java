package com.prgpr.data;

import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.Property;
import com.prgpr.framework.database.SearchProvider;
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

    public enum MonumentAttribute implements Property { }

    public Monument(Element node) {
        this.node = node;
    }

    public Monument(EmbeddedDatabase db, Page page) {
        this.ownNamespaceID = 18; // As wiki namespaces hasn't got the namespaceid 18, lets take this.
        this.node = db.createElement(indexName, hashCode(page.getTitle(), ownNamespaceID), (node) -> {
            node.addLabel(EntityTypes.Monument);
            node.setProperty(EntityAttribute.title, page.getTitle());
            node.setProperty(EntityAttribute.ownNamespaceID, ownNamespaceID);
        });
        this.source = SearchProvider.findNode(db, EntityTypes.Page, Page.PageAttribute.hash, page.getHashCode());
        insertSourceLink();
    }

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

}
