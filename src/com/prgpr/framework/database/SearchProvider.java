package com.prgpr.framework.database;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by strange on 11/20/16.
 * @author Noah Hummel
 *
 * A Class providing basic search functionality for neo4j.
 */
public class SearchProvider {

    // *****************************************************************************************************************
    // Node search

    /**
     * Find all nodes that match one label (maybe more) and any of the properties.
     * @param db
     * @param label
     * @param properties
     * @return
     */
    public static Set<Element> findAnyWithLabel(
            EmbeddedDatabase db,
            Label label,
            Set<PropertyValuePair> properties){

        return db.getAllElements()
                .filter(n -> NodePredicates.matchesLabel(n, label))
                .filter(n -> NodePredicates.matchesAnyProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Find a node that matches a label (maybe more) and a property.
     * @param db
     * @param label
     * @param property
     * @param val
     * @param <E>
     * @return
     */
    public static <E> Element findNode(EmbeddedDatabase db, Label label, Property property, E val){
        return db.findElements(label, new PropertyValuePair<>(property, val))
                .findFirst().orElse(null);
    }

    // *****************************************************************************************************************
    // Neighbor Traversals

    public static Set<Element> findImmediateOutgoing(
            Element start,
            RelationshipType relType
    ){
        return start
                .getDatabase()
                .getTraversalProvider()
                .traverseUniqueToDepth(start, relType, TraversalProvider.Direction.OUTGOING, 1)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Element> findImmediateIncoming(
            Element start,
            RelationshipType relType
    ){
        return start
                .getDatabase()
                .getTraversalProvider()
                .traverseUniqueToDepth(start, relType, TraversalProvider.Direction.INCOMING, 1)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // *****************************************************************************************************************
    // Complete Graph Traversals

    public static Set<Element> findAllInSubgraph(
        Element start,
        RelationshipType relType
    ){
        return  start
                .getDatabase()
                .getTraversalProvider()
                .traverseUnique(start, relType, TraversalProvider.Direction.OUTGOING)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
