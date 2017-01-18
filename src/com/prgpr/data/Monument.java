package com.prgpr.data;

import com.prgpr.Metadata.MonumentDataExtractor;
import com.prgpr.framework.AsciiTable;
import com.prgpr.framework.database.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Monument Class.
 * It represents a Monument, sourcing from a Wikipedia Page.
 *
 * @author Elizaveta Kovalevskaya, Noah Hummel
 */
public class Monument extends EntityBase {

    private static final Logger log = LogManager.getFormatterLogger(Monument.class);
    private static final String indexName = "Monuments";

    public enum MonumentAttribute implements Property {
        NAME,
        CREATION_DATE,
        INAUGURATION_DATE
    }

    public Monument(Element node) {
        this.node = node;
        Set<Element> sources = SearchProvider.findImmediateOutgoing(node, RelationshipTypes.sourceLink);
        assert !sources.isEmpty();
        this.source = (Element) sources.toArray()[0];
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

    public void extractMetadata() {
        this.node.setProperty(MonumentAttribute.NAME, MonumentDataExtractor.extractName(this));

        if (this.node.getProperty(EntityAttribute.entityId) == null) return;  // just give up
        this.node.setProperty(MonumentAttribute.CREATION_DATE, MonumentDataExtractor.extractCreationDate(this));
        this.node.setProperty(MonumentAttribute.INAUGURATION_DATE, MonumentDataExtractor.extractDateOfInauguration(this));

        Person commemorated = MonumentDataExtractor.extractCommemoratedPerson(this);
        if (commemorated != null) {
            Element personNode = SearchProvider.findNode(this.node.getDatabase(), EntityTypes.Person, EntityAttribute.hash, commemorated.hashCode());
            if (personNode != null) this.node.createUniqueRelationshipTo(personNode, RelationshipTypes.commemoratedPersonLink);
        }

        City location = MonumentDataExtractor.extractNearestCity(this);
        if (location != null) {
            Element cityNode = SearchProvider.findNode(this.node.getDatabase(), EntityTypes.City, EntityAttribute.hash, location.hashCode());
            if (cityNode != null) this.node.createUniqueRelationshipTo(cityNode, RelationshipTypes.locationLink);
        }
    }

    public String getName() {
        return (String)this.node.getProperty(MonumentAttribute.NAME);
    }

    public String getCreationDate() {
        return (String)this.node.getProperty(MonumentAttribute.CREATION_DATE);
    }

    public String getInaugurationDate() {
        return (String)this.node.getProperty(MonumentAttribute.INAUGURATION_DATE);
    }

    public Person getCommemoratedPerson() {
        Set<Person> commemoratedPersons = SearchProvider.findImmediateOutgoing(this.node, RelationshipTypes.commemoratedPersonLink)
                .stream()
                .map(Person::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (!commemoratedPersons.isEmpty()) return commemoratedPersons.iterator().next();
        return null;
    }

    public City getNearestCity() {
        Set<City> nearestCities = SearchProvider.findImmediateOutgoing(this.node, RelationshipTypes.locationLink)
                .stream()
                .map(City::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (!nearestCities.isEmpty()) return nearestCities.iterator().next();
        return null;
    }

    @Override
    public String toString() {
        AsciiTable t = new AsciiTable();

        t.setColumns(
                "Name",
                "Creation Date",
                "Inauguration Date",
                "Commemorated Person",
                "NearestCity"
        );

        t.addRow(
                getName(),
                getCreationDate(),
                getInaugurationDate(),
                getCommemoratedPerson(),
                getNearestCity()
        );

        return t.toString();
    }
}
