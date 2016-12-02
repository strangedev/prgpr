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

    public static Set<Element> findAll(
            EmbeddedDatabase db,
            Set<Label> labels,
            Set<PropertyValuePair> properties){

        return db.getAllElements()
                .filter(n -> NodePredicates.matchesAllLabels(n, labels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Element> findAny(
            EmbeddedDatabase db,
            Set<Label> labels,
            Set<PropertyValuePair> properties){
        return db.getAllElements()
                .filter(n -> NodePredicates.matchesAnyLabel(n, labels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Element> findAnyWithLabel(
            EmbeddedDatabase db,
            Label labels,
            Set<PropertyValuePair> properties){

        return db.getAllElements()
                .filter(n -> NodePredicates.matchesLabel(n, labels))
                .filter(n -> NodePredicates.matchesAnyProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Set<Element> findNode(
            EmbeddedDatabase db,
            Label label,
            Set<PropertyValuePair> properties){

        return db.getAllElements()
                .filter(n -> NodePredicates.matchesLabel(n, label))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static <E> Element findNode(EmbeddedDatabase db, Label label, Property property, E val){
        return db.findElements(label, new PropertyValuePair<>(property, val))
                .findFirst().orElse(null);
    }

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
