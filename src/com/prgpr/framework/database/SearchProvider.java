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
     * Find all nodes that match all labels and properties.
     *
     * @param db
     * @param labels
     * @param properties
     * @return
     */
    public static Set<Element> findAll(
            EmbeddedDatabase db,
            Set<Label> labels,
            Set<PropertyValuePair> properties){

        return db.getAllElements()
                .filter(n -> NodePredicates.matchesAllLabels(n, labels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Find all nodes that match any of the labels and all of the properties.
     *
     * @param db
     * @param labels
     * @param properties
     * @return
     */
    public static Set<Element> findAny(
            EmbeddedDatabase db,
            Set<Label> labels,
            Set<PropertyValuePair> properties){
        return db.getAllElements()
                .filter(n -> NodePredicates.matchesAnyLabel(n, labels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

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
     * Find all nodes that match one labels (maybe more) and all of the properties.
     * @param db
     * @param label
     * @param properties
     * @return
     */
    public static Set<Element> findAllWithLabel(
            EmbeddedDatabase db,
            Label label,
            Set<PropertyValuePair> properties){

        return db.getAllElements()
                .filter(n -> NodePredicates.matchesLabel(n, label))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
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

    // Multiple Labels, multiple properties, multiple relationship types

    public static Set<Element> findImmediateOutgoing(
            Element start,
            Set<Label> nodeLabels,
            List<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        return start.getDatabase().getTraversalProvider()
                .traverseIncomingUnique(start, relTypes, 1)
                .filter(n -> NodePredicates.matchesAllLabels(n, nodeLabels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Element> findImmediateIncoming(
            Element start,
            Set<Label> nodeLabels,
            List<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        return start.getDatabase()
                .getTraversalProvider()
                .traverseIncomingUnique(start, relTypes, 1)
                .filter(n -> NodePredicates.matchesAnyLabel(n, nodeLabels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // Only one Label, multiple Properties, only one relationship type:

    public static Set<Element> findImmediateOutgoing(
            Element start,
            Label nodeLabel,
            RelationshipType relType,
            Set<PropertyValuePair> properties
    ){
        return start.getDatabase()
                .getTraversalProvider()
                .traverseOutgoingUnique(start, relType, 1)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Element> findImmediateIncoming(
            Element start,
            Label nodeLabel,
            RelationshipType relType,
            Set<PropertyValuePair> properties
    ){
        return start.getDatabase()
                .getTraversalProvider()
                .traverseIncomingUnique(start, relType, 1)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // Only one Label, only one Property, only one relationship type

    public static Set<Element> findImmediateOutgoing(
            Element start,
            Label nodeLabel,
            RelationshipType relType,
            PropertyValuePair property
    ){
        return start.getDatabase()
                .getTraversalProvider()
                .traverseOutgoingUnique(start, relType, 1)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesProperty(n, property))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Element> findImmediateIncoming(
            Element start,
            Label nodeLabel,
            RelationshipType relType,
            PropertyValuePair property
    ){
        return start
                .getDatabase()
                .getTraversalProvider()
                .traverseIncomingUnique(start, relType, 1)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesProperty(n, property))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // *****************************************************************************************************************
    // Complete Graph Traversals

    public static Set<Element> findAllInSubgraph(
        Element start,
        Label nodeLabel,
        RelationshipType relType,
        PropertyValuePair property
    ){
        return  start
                .getDatabase()
                .getTraversalProvider()
                .traverseOutgoingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesProperty(n, property))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
