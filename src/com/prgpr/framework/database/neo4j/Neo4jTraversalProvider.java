package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.RelationshipType;
import com.prgpr.framework.database.TraversalProvider;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by strange on 11/21/16.
 * @author Noah Hummel
 *
 * Provides basic TraversalDescriptions for neo4j.
 */
public class Neo4jTraversalProvider extends TraversalProvider {

    private GraphDatabaseService db;

    public Neo4jTraversalProvider(GraphDatabaseService db) {
        this.db = db;
    }

    @Override
    protected Stream<Element> getUniqueDepthLimitedTraversal(Element from, List<RelationshipType> relTypes, int depth, Direction direction) {
        TraversalDescription tv = getTraversalDescription(relTypes, direction);
        tv.evaluator(Evaluators.atDepth(depth));
        return traverse((Neo4jElement) from, tv);
    }

    @Override
    protected Stream<Element> getUniqueTraversal(Element from, List<RelationshipType> relTypes, Direction direction) {
        TraversalDescription tv = getTraversalDescription(relTypes, direction);
        tv.evaluator(Evaluators.excludeStartPosition());
        return traverse((Neo4jElement) from, tv);
    }

    private Stream<Element> traverse(Neo4jElement element, TraversalDescription tv){
        return tv.traverse(element.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(element.getDatabase(), n));
    }

    private TraversalDescription getTraversalDescription(List<RelationshipType> relTypes, Direction direction) {
        TraversalDescription tv = db.traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        org.neo4j.graphdb.Direction n4jDirection = org.neo4j.graphdb.Direction.valueOf(direction.name());
        relTypes.forEach(r -> tv.relationships(org.neo4j.graphdb.RelationshipType.withName(r.name()), n4jDirection));
        return tv;
    }
}
