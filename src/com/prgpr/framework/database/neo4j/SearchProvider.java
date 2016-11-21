package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.*;

import java.util.*;

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

        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getAllNodes()
                .filter(n -> NodePredicates.matchesAllLabels(n, labels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findAny(
            EmbeddedDatabase db,
            Set<Label> labels,
            Set<PropertyValuePair> properties){

        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getAllNodes()
                .filter(n -> NodePredicates.matchesAnyLabel(n, labels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findNode(
            EmbeddedDatabase db,
            Label label,
            Set<PropertyValuePair> properties){

        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getAllNodes()
                .filter(n -> NodePredicates.matchesLabel(n, label))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findNode(
            EmbeddedDatabase db,
            Label label,
            PropertyValuePair property){

        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.findElements(label, property)
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findImmediateOutgoing(
            Neo4jElement start,
            Set<Label> nodeLabels,
            List<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        EmbeddedDatabase db = start.getDatabase();
        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getTraversalProvider()
                .traverseIncomingUnique(start, relTypes)
                .filter(n -> NodePredicates.matchesAllLabels(n, nodeLabels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findAnyImmediateIncoming(
            Neo4jElement start,
            Set<Label> nodeLabels,
            List<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        EmbeddedDatabase db = start.getDatabase();
        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getTraversalProvider()
                .traverseIncomingUnique(start, relTypes)
                .filter(n -> NodePredicates.matchesAnyLabel(n, nodeLabels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findImmediateOutgoing(
            Neo4jElement start,
            Label nodeLabel,
            RelationshipType relType,
            Set<PropertyValuePair> properties
    ){
        EmbeddedDatabase db = start.getDatabase();
        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getTraversalProvider()
                .traverseOutgoingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findAnyImmediateIncoming(
            Neo4jElement start,
            Label nodeLabel,
            RelationshipType relType,
            Set<PropertyValuePair> properties
    ){
        EmbeddedDatabase db = start.getDatabase();
        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getTraversalProvider()
                .traverseIncomingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findImmediateOutgoing(
            Neo4jElement start,
            Label nodeLabel,
            RelationshipType relType,
            PropertyValuePair property
    ){
        EmbeddedDatabase db = start.getDatabase();
        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getTraversalProvider()
                .traverseOutgoingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesProperty(n, property))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Element> findAnyImmediateIncoming(
            Element start,
            Label nodeLabel,
            RelationshipType relType,
            PropertyValuePair property
    ){
        EmbeddedDatabase db = start.getDatabase();
        db.transaction();
        Set<Element> ret = new LinkedHashSet<>();

        db.getTraversalProvider()
                .traverseIncomingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesProperty(n, property))
                .forEach(ret::add);

        return ret;
    }

}
