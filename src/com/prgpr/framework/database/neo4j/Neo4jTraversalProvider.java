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
    protected Stream<Element> getUniqueTraversal(Element from, List<RelationshipType> relTypes, int depth, Direction direction) {
        Node node = ((Neo4jElement)from).getNode(); // uhh it works -_(^_^)_-

        TraversalDescription tv = db.traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL)
                .evaluator(Evaluators.atDepth(depth));

        org.neo4j.graphdb.Direction n4jDirection = org.neo4j.graphdb.Direction.valueOf(direction.name());
        relTypes.forEach(r -> tv.relationships((org.neo4j.graphdb.RelationshipType)r, n4jDirection)); // <(^.^<) Check it out!

        return tv.traverse(node)
                .nodes()
                .stream()
                .map(n -> new Neo4jElement((Neo4jEmbeddedDatabase)from.getDatabase(), n));
    }
}
