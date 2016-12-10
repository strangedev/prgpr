package com.prgpr.framework.database;

import org.neo4j.graphdb.Relationship;

import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 *
 * An abstract database Element a.k.a. a graph node.
 * Elements are typed (labeled) and have an arbitrary amount of properties.
 *
 * @author Kyle Rinfreschi
 */
public interface Element {

    /**
     * Adds a label to the Element.
     * Labels can be viewed as the type of the Element.
     * Elements can have no labels, or an arbitrary amount of labels.
     *
     * @param label The label to apply to the Element.
     */
    void addLabel(Label label);

    /**
     * @return A Stream of all labels applied to this Element.
     */
    Stream<Label> getLabels();

    /**
     * Gets the value of one of the Element's properties by property description.
     * Properties should be enumerated somewhere accessible to all Objects related to the Elements using it.
     *
     * @param property The property for which to get the value.
     * @return The value of the property.
     */
    Object getProperty(Property property);

    /**
     * Sets the value of one of the Element's properties by property description.
     * Properties should be enumerated somewhere accessible to all Objects related to the Elements using it.
     *
     * @param property The property to set the value of.
     * @param val The value of the property.
     * @param <E> The type of the value.
     */
    <E> void setProperty(Property property, E val);

    /**
     * Creates a  unique relationship a.k.a. an edge from this Element to another.
     * Relationships are typed, so the type of the relationship has to be passed as well.
     * If a relationship from this Element to the given Element of the given type does already exist,
     * no action is performed and the already existing relationship is returned.
     *
     * @param to The Element to which to create a relationship to.
     * @param relationshipType The type of the new relationship.
     * @return The created or already existing relationship.
     */
    Relationship createUniqueRelationshipTo(Element to, RelationshipType relationshipType);

    /**
     * @param callback update a number of properties and labels of a single element at once.
     */
    void update(Callback<Element> callback);

    /**
     * @return The database to which this element belongs.
     */
    EmbeddedDatabase getDatabase();

}
