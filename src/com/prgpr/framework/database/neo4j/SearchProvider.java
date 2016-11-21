package com.prgpr.framework.database;

import org.neo4j.graphdb.*;

import java.util.*;

/**
 * Created by strange on 11/20/16.
 * @author Noah Hummel
 *
 * A Class providing basic search functionality for neo4j.
 */
public class SearchProvider {

    public static Set<Node> findAll(
            GraphDatabaseService db,
            Set<Label> labels,
            Set<PropertyValuePair> properties){

        TransactionManager.getTransaction(db);
        Set<Node> ret = new LinkedHashSet<>();

        db.getAllNodes()
                .stream()
                .filter(n -> NodePredicates.matchesAllLabels(n, labels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findAny(
            GraphDatabaseService db,
            Set<Label> labels,
            Set<PropertyValuePair> properties){

        TransactionManager.getTransaction(db);
        Set<Node> ret = new LinkedHashSet<>();

        db.getAllNodes()
                .stream()
                .filter(n -> NodePredicates.matchesAnyLabel(n, labels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findNode(
            GraphDatabaseService db,
            Label label,
            Set<PropertyValuePair> properties){

        TransactionManager.getTransaction(db);
        Set<Node> ret = new LinkedHashSet<>();

        db.getAllNodes()
                .stream()
                .filter(n -> NodePredicates.matchesLabel(n, label))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findNode(
            GraphDatabaseService db,
            Label label,
            PropertyValuePair property){

        TransactionManager.getTransaction(db);
        Set<Node> ret = new LinkedHashSet<>();

        db.findNodes(label, property.property.name(), property.value)
                .stream()
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findImmediateOutgoing(
            Node start,
            Set<Label> nodeLabels,
            Set<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        TransactionManager.getTransaction(start.getGraphDatabase());
        Set<Node> ret = new LinkedHashSet<>();

        TraversalProvider.traverseOutgoingUnique(start, relTypes)
                .filter(n -> NodePredicates.matchesAllLabels(n, nodeLabels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findAnyImmediateIncoming(
            Node start,
            Set<Label> nodeLabels,
            Set<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        TransactionManager.getTransaction(start.getGraphDatabase());
        Set<Node> ret = new LinkedHashSet<>();

        TraversalProvider.traverseIncomingUnique(start, relTypes)
                .filter(n -> NodePredicates.matchesAnyLabel(n, nodeLabels))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findImmediateOutgoing(
            Node start,
            Label nodeLabel,
            RelationshipType relType,
            Set<PropertyValuePair> properties
    ){
        TransactionManager.getTransaction(start.getGraphDatabase());
        Set<Node> ret = new LinkedHashSet<>();

        TraversalProvider.traverseOutgoingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findAnyImmediateIncoming(
            Node start,
            Label nodeLabel,
            RelationshipType relType,
            Set<PropertyValuePair> properties
    ){
        TransactionManager.getTransaction(start.getGraphDatabase());
        Set<Node> ret = new LinkedHashSet<>();

        TraversalProvider.traverseIncomingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findImmediateOutgoing(
            Node start,
            Label nodeLabel,
            RelationshipType relType,
            PropertyValuePair property
    ){
        TransactionManager.getTransaction(start.getGraphDatabase());
        Set<Node> ret = new LinkedHashSet<>();

        TraversalProvider.traverseOutgoingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesProperty(n, property))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findAnyImmediateIncoming(
            Node start,
            Label nodeLabel,
            RelationshipType relType,
            PropertyValuePair property
    ){
        TransactionManager.getTransaction(start.getGraphDatabase());
        Set<Node> ret = new LinkedHashSet<>();

        TraversalProvider.traverseIncomingUnique(start, relType)
                .filter(n -> NodePredicates.matchesLabel(n, nodeLabel))
                .filter(n -> NodePredicates.matchesProperty(n, property))
                .forEach(ret::add);

        return ret;
    }

}
