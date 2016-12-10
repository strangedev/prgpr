package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.RelationshipType;
import com.prgpr.framework.database.TraversalProvider;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.traversal.*;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by strange on 11/21/16.
 *
 * Provides basic TraversalDescriptions for neo4j.
 *
 * @author Noah Hummel
 * Docstrings are kept to a minimum, since all overridden methods are described in
 * detail in the superclass.
 */
public class Neo4jTraversalProvider extends TraversalProvider {

    private GraphDatabaseService db;

    Neo4jTraversalProvider(GraphDatabaseService db) {
        this.db = db;
    }

    @Override
    protected Stream<Element> getUniqueDepthLimitedTraversal(Element from, List<RelationshipType> relTypes, int depth, Direction direction) {
        TraversalDescription tv = getTraversalDescription(relTypes, direction)
                                        .evaluator(Evaluators.excludeStartPosition())  // exclude the starting node
                                        .evaluator(Evaluators.toDepth(depth));  // Only consider nodes up to a certain depth
        return traverse((Neo4jElement) from, tv);
    }

    @Override
    protected Stream<Element> getUniqueTraversal(Element from, List<RelationshipType> relTypes, Direction direction) {
        TraversalDescription tv = getTraversalDescription(relTypes, direction)
                                        .evaluator(Evaluators.excludeStartPosition())  // exclude the starting node
                                        .evaluator(Evaluators.all());  // consider all nodes in traversal
        return traverse((Neo4jElement) from, tv);
    }

    /**
     * Takes a traversal description, starts the traversal and returns the resulting iterator as a stream
     * of Elements.
     *
     * @param element The element from which to start the traversal.
     * @param tv The traversal description which to execute.
     * @return A stream of Elements contained in the given traversal.
     */
    private Stream<Element> traverse(Neo4jElement element, TraversalDescription tv){
        return tv.traverse(element.getNode())
                .nodes()
                .stream()
                .map(n -> new Neo4jElement(element.getDatabase(), n));
    }

    /**
     * Assembles a traversal description from a given List of allowed RelationshipTypes and allowed traversal
     * direction. The generated description is breadth first und has global node uniqueness.
     *
     * @param relTypes A List of allowed RelationshipTypes.
     * @param direction The allowed traversal direction.
     * @return The assembled traversal description.
     */
    private TraversalDescription getTraversalDescription(List<RelationshipType> relTypes, Direction direction) {
        TraversalDescription tv = db.traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        org.neo4j.graphdb.Direction n4jDirection = org.neo4j.graphdb.Direction.valueOf(direction.name());

        for(RelationshipType type : relTypes){
            tv = tv.relationships(org.neo4j.graphdb.RelationshipType.withName(type.name()), n4jDirection);
        }

        return tv;
    }
}
