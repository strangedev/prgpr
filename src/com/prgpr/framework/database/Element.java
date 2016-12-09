package com.prgpr.framework.database;

import org.neo4j.graphdb.Relationship;

import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 */
public interface Element {

    /**
     * Assigns a label to the element.
     * @param label the label of the element
     */
    void addLabel(Label label);

    /**
     * @return a stream of current assigned labels.
     */
    Stream<Label> getLabels();

    /**
     * @param property property of the element
     * @return the current value assigned to the requested property
     */
    Object getProperty(Property property);

    /**
     * @param <E> the template type
     * @param property the given property
     * @param val the value to be assigned to the given property
     */
    <E> void setProperty(Property property, E val);

    /**
     * Update multiple properties and values of a node simultaneously
     * @param callback a function to be called
     */
    void update(Callback<Element> callback);

    //@TODO:
    Relationship createUniqueRelationshipTo(Element ingoing, RelationshipType relation);

    /**
     * @return the database the element belongs to.
     */
    EmbeddedDatabase getDatabase();

}
