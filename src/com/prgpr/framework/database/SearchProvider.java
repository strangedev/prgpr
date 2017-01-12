package com.prgpr.framework.database;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by strange on 11/20/16.
 *
 * A Class providing basic search functionality for neo4j.
 * begin rant
 *     Why would you even design a database without shipping the most basic search functionality?
 *     I mean neo4j makes even mysql look like the most advanced piece of tech ever devised by mankind.
 * end rant
 *
 * @author Noah Hummel
 */
public class SearchProvider {

    // *****************************************************************************************************************
    // * Searching for single Elements                                                                                 *
    // *****************************************************************************************************************

    /**
     * Finds all elements that match one (or more) label(s) and any of the given properties.
     *
     * @param db The database to search
     * @param label The label to filter with
     * @param properties The set of properties to filter with
     * @return The set of matching elements within the database
     */
    public static Set<Element> findAnyWithLabel(
            EmbeddedDatabase db,
            Label label,
            Set<PropertyValuePair> properties){

        return db.transaction(() ->
            db.getAllElements()
                .filter(n -> NodePredicates.matchesLabel(n, label))
                .filter(n -> NodePredicates.matchesAnyProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    /**
     * Find an Element that matches one (or more) label(s) and one (or more) property.
     *
     * @param db The database to search
     * @param label The label to filter with
     * @param property The name of the property to filter with
     * @param val The value of the property to filter with
     * @param <E> the type of the value
     * @return The first matching Element
     */
    public static <E> Element findNode(EmbeddedDatabase db, Label label, Property property, E val){
        return db.transaction(() ->
                db.findElements(label, new PropertyValuePair<>(property, val))
                .findFirst().orElse(null)
        );
    }

    // *****************************************************************************************************************
    // * Searching for neighbors                                                                                       *
    // *****************************************************************************************************************

    /**
     * Finds all Element neighboring a start Element, connected by an outgoing relationship of a certain type.
     *
     * @param start The node from which to start the search
     * @param relType The relationship type
     * @return The set of neighboring Elements
     */
    public static Set<Element> findImmediateOutgoing(
            Element start,
            RelationshipType relType
    ){
        EmbeddedDatabase db = start.getDatabase();
        return db.transaction(() ->
            start.getDatabase()
                .getTraversalProvider()
                .traverseUniqueToDepth(start, relType, TraversalProvider.Direction.OUTGOING, 1)
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    /**
     Finds all Element neighboring a start Element, connected by an incoming relationship of a certain type.
     *
     * @param start The node from which to start the search
     * @param relType The relationship type
     * @return The set of neighboring Elements
     */
    public static Set<Element> findImmediateIncoming(
            Element start,
            RelationshipType relType
    ){
        EmbeddedDatabase db = start.getDatabase();
        return db.transaction(() ->
            start.getDatabase()
                .getTraversalProvider()
                .traverseUniqueToDepth(start, relType, TraversalProvider.Direction.INCOMING, 1)
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    // *****************************************************************************************************************
    // * Searching entire subgraphs                                                                                    *
    // *****************************************************************************************************************

    /**
     * Finds all Elements within a connected component containing a given Element which only includes Relationships
     * of a certain type and direction.
     *
     * @param start The node from which to start the search
     * @param relType The relationship type
     * @return The set of Elements in the subgraph
     */
    public static Set<Element> findAllInSubgraph(
        Element start,
        RelationshipType relType
    ){
        EmbeddedDatabase db = start.getDatabase();
        return db.transaction(() ->
                start.getDatabase()
                .getTraversalProvider()
                .traverseUnique(start, relType, TraversalProvider.Direction.OUTGOING)
                .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

}
