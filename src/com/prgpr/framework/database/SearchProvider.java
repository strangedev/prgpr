package com.prgpr.framework.database;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * Created by strange on 11/20/16.
 */
public class SearchProvider {

    /*public static Node findAll(
            GraphDatabaseService db,
            Set<Label> labels,
            Set<PropertyValuePair> properties){

    }*/

    public static Set<Node> findImmediateOutgoing(
            GraphDatabaseService db,
            Node start,
            Set<Label> nodeLabels,
            Set<RelationshipType> relTypes,
            Set<PropertyValuePair> properties
    ){
        TransactionManager.getTransaction(db);
        Set<Node> ret = new LinkedHashSet<>();


        TraversalDescription neighborTraversal = db.traversalDescription()
                .breadthFirst()
                .uniqueness(Uniqueness.NODE_GLOBAL);

        relTypes.forEach(r -> neighborTraversal.relationships(r, Direction.OUTGOING));

        neighborTraversal
                .evaluator(Evaluators.atDepth(1))
                .traverse(start)
                .nodes()
                .stream()
                .filter(n ->
                        StreamSupport.stream(n.getLabels().spliterator(), false)
                        .anyMatch(nodeLabels::contains)
                        || nodeLabels.isEmpty()
                )
                .filter(n ->
                        properties
                                .stream()
                                .allMatch(p ->
                                    n.getProperty(p.property.name()) == p.value
                                )
                        || properties.isEmpty()
                )
                .forEach(ret::add);

        return ret;
    }


}
