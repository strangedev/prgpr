package com.prgpr.data;

import com.prgpr.Metadata.CityDataExtractor;
import com.prgpr.framework.AsciiTable;
import com.prgpr.framework.database.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
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

    public enum CityAttribute implements Property {
        NAME,
        COUNTRY,
        POPULATION,
        EARLIEST_MENTION
    }

    public City(Element node) {
        this.node = node;
        Set<Element> sources = SearchProvider.findImmediateOutgoing(node, RelationshipTypes.sourceLink);
        assert !sources.isEmpty();
        this.source = (Element) sources.toArray()[0];
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

    public void extractMetadata() {
        this.node.setProperty(CityAttribute.NAME, CityDataExtractor.extractName(this));

        if (this.node.getProperty(EntityAttribute.entityId) == null) return;  // just give up
        this.node.setProperty(CityAttribute.COUNTRY, CityDataExtractor.extractCountry(this));
        this.node.setProperty(CityAttribute.POPULATION, CityDataExtractor.extractPopulation(this));
        this.node.setProperty(CityAttribute.EARLIEST_MENTION, CityDataExtractor.extractEarliestMention(this));
    }

    public String getName() {
        return (String)this.node.getProperty(CityAttribute.NAME);
    }

    public String getCountry() {
        return (String)this.node.getProperty(CityAttribute.COUNTRY);
    }

    public String getPopulation() {
        return (String)this.node.getProperty(CityAttribute.POPULATION);
    }

    public String getEarliestMention() {
        return (String)this.node.getProperty(CityAttribute.EARLIEST_MENTION);
    }

    @Override
    public String toString() {
        AsciiTable t = new AsciiTable();

        t.setColumns(
                "Name",
                "Country",
                "Population",
                "Earliest Mention"
        );

        t.addRow(
                getName(),
                getCountry(),
                getPopulation(),
                getEarliestMention()
        );

        return t.toString();
    }
}
