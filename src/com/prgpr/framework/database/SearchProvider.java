package com.prgpr.framework.database;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by strange on 11/20/16.
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
                .filter(n -> matchAllLabels(n, labels))
                .filter(n -> matchAllProperties(n, properties))
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
                .filter(n -> matchAnyLabel(n, labels))
                .filter(n -> matchAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    //public static Optional<Node> findNode(){return new Node()}

    public static Set<Node> findImmediateOutgoing(
            GraphDatabaseService db,
            Node start,
            Set<Label> nodeLabels,
            Set<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        TransactionManager.getTransaction(db);
        Set<Node> ret = new LinkedHashSet<>();

        traverseOutgoingUnique(db, start, relTypes)
                .filter(n -> matchAllLabels(n, nodeLabels))
                .filter(n -> matchAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    public static Set<Node> findAnyImmediateOutgoing(
            GraphDatabaseService db,
            Node start,
            Set<Label> nodeLabels,
            Set<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        TransactionManager.getTransaction(db);
        Set<Node> ret = new LinkedHashSet<>();

        traverseOutgoingUnique(db, start, relTypes)
                .filter(n -> matchAnyLabel(n, nodeLabels))
                .filter(n -> matchAllProperties(n, properties))
                .forEach(ret::add);

        return ret;
    }

    private static Stream<Node> traverseOutgoingUnique(GraphDatabaseService db, Node from, Set<RelationshipType> relTypes){
        TraversalDescription tv = db.traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> tv.relationships(r, Direction.OUTGOING));
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from)
                .nodes()
                .stream();
    }

    private static Stream<Node> traverseOutgoingUnique(GraphDatabaseService db, Node from, Set<RelationshipType> relTypes, int depth){
        TraversalDescription tv = db.traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> tv.relationships(r, Direction.OUTGOING));
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from)
                .nodes()
                .stream();
    }

    private static Stream<Node> traverseIncomingUnique(GraphDatabaseService db, Node from, Set<RelationshipType> relTypes){
        TraversalDescription tv = db.traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> tv.relationships(r, Direction.INCOMING));
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from)
                .nodes()
                .stream();
    }

    private static Stream<Node> traverseIncomingUnique(GraphDatabaseService db, Node from, Set<RelationshipType> relTypes, int depth){
        TraversalDescription tv = db.traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> tv.relationships(r, Direction.INCOMING));
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from)
                .nodes()
                .stream();
    }

    private static boolean matchAnyLabel(Node n, Set<Label> labels){
        return StreamSupport.stream(n.getLabels().spliterator(), false)
                            .anyMatch(labels::contains)
                            || labels.isEmpty();
    }

    private static boolean matchAllLabels(Node n, Set<Label> labels){
        return StreamSupport.stream(n.getLabels().spliterator(), false)
                            .allMatch(labels::contains)
                            || labels.isEmpty();
    }

    private static boolean matchLabel(Node n, Label label){
        return StreamSupport.stream(n.getLabels().spliterator(), false)
                            .anyMatch(l -> l.name().equals(label.name()))
                            || label.name().isEmpty();
    }

    private static boolean matchAllProperties(Node n, Set<PropertyValuePair> properties){
         return properties.stream()
                          .allMatch(p ->
                                  n.getProperty(p.property.name()) == p.value
                          ) || properties.isEmpty();
    }

}
