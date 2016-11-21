package com.prgpr.framework.database;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by strange on 11/21/16.
 * @author Noah Hummel
 *
 * Provides basic TraversalDescriptions for neo4j.
 */
public class TraversalProvider {

    public static Stream<Node> traverseOutgoingUnique(Node from, Set<RelationshipType> relTypes){
        TraversalDescription tv = from.getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> tv.relationships(r, Direction.OUTGOING));
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from)
                .nodes()
                .stream();
    }

    public static Stream<Node> traverseOutgoingUnique(Node from, Set<RelationshipType> relTypes, int depth){
        TraversalDescription tv = from.getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> tv.relationships(r, Direction.OUTGOING));
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from)
                .nodes()
                .stream();
    }

    public static Stream<Node> traverseOutgoingUnique(Node from, RelationshipType relType){
        TraversalDescription tv = from.getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        tv.relationships(relType, Direction.OUTGOING);
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from)
                .nodes()
                .stream();
    }

    public static Stream<Node> traverseOutgoingUnique(Node from, RelationshipType relType, int depth){
        TraversalDescription tv = from.getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        tv.relationships(relType, Direction.OUTGOING);
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from)
                .nodes()
                .stream();
    }


    public static Stream<Node> traverseIncomingUnique(Node from, Set<RelationshipType> relTypes){
        TraversalDescription tv = from.getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> tv.relationships(r, Direction.INCOMING));
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from)
                .nodes()
                .stream();
    }

    public static Stream<Node> traverseIncomingUnique(Node from, Set<RelationshipType> relTypes, int depth){
        TraversalDescription tv = from.getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> tv.relationships(r, Direction.INCOMING));
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from)
                .nodes()
                .stream();
    }

    public static Stream<Node> traverseIncomingUnique(Node from, RelationshipType relType){
        TraversalDescription tv = from.getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        tv.relationships(relType, Direction.INCOMING);
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from)
                .nodes()
                .stream();
    }

    public static Stream<Node> traverseIncomingUnique(Node from, RelationshipType relType, int depth){
        TraversalDescription tv = from.getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        tv.relationships(relType, Direction.INCOMING);
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from)
                .nodes()
                .stream();
    }
    
}
