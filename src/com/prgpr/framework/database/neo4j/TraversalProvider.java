package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.Element;
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

    public static Stream<Neo4jElement> traverseOutgoingUnique(Neo4jElement from, Set<RelationshipType> relTypes){
        TraversalDescription tv = getTraversalDescription(from);

        relTypes.forEach(r -> tv.relationships(r, Direction.OUTGOING));
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(from.getDatabase(), n));
    }

    public static Stream<Neo4jElement> traverseOutgoingUnique(Neo4jElement from, Set<RelationshipType> relTypes, int depth){
        TraversalDescription tv = getTraversalDescription(from);

        relTypes.forEach(r -> tv.relationships(r, Direction.OUTGOING));
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(from.getDatabase(), n));
    }

    public static Stream<Neo4jElement> traverseOutgoingUnique(Neo4jElement from, RelationshipType relType){
        TraversalDescription tv = getTraversalDescription(from);

        tv.relationships(relType, Direction.OUTGOING);
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(from.getDatabase(), n));
    }

    public static Stream<Neo4jElement> traverseOutgoingUnique(Neo4jElement from, RelationshipType relType, int depth){
        TraversalDescription tv = getTraversalDescription(from);

        tv.relationships(relType, Direction.OUTGOING);
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(from.getDatabase(), n));
    }


    public static Stream<Neo4jElement> traverseIncomingUnique(Neo4jElement from, Set<RelationshipType> relTypes){
        TraversalDescription tv = getTraversalDescription(from);

        relTypes.forEach(r -> tv.relationships(r, Direction.INCOMING));
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(from.getDatabase(), n));
    }

    public static Stream<Neo4jElement> traverseIncomingUnique(Neo4jElement from, Set<RelationshipType> relTypes, int depth){
        TraversalDescription tv = getTraversalDescription(from);

        relTypes.forEach(r -> tv.relationships(r, Direction.INCOMING));
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(from.getDatabase(), n));
    }

    public static Stream<Neo4jElement> traverseIncomingUnique(Neo4jElement from, RelationshipType relType){
        TraversalDescription tv = getTraversalDescription(from);

        tv.relationships(relType, Direction.INCOMING);
        return tv.evaluator(Evaluators.atDepth(1))
                .traverse(from.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(from.getDatabase(), n));
    }

    public static Stream<Neo4jElement> traverseIncomingUnique(Neo4jElement from, RelationshipType relType, int depth){
        TraversalDescription tv = getTraversalDescription(from);

        tv.relationships(relType, Direction.INCOMING);
        return tv.evaluator(Evaluators.atDepth(depth))
                .traverse(from.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(from.getDatabase(), n));
    }

    private static TraversalDescription getTraversalDescription(Neo4jElement from) {
        return from.getNode().getGraphDatabase().traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);
    }

}
