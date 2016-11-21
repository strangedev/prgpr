package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.*;
import com.prgpr.framework.database.Label;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.UniqueFactory;

import java.io.File;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 */
public class Neo4jEmbeddedDatabase implements EmbeddedDatabase {

    private Neo4jTraversalProvider traversalProvider;
    private GraphDatabaseService graphDb;

    public Neo4jEmbeddedDatabase() {}

    public Neo4jEmbeddedDatabase(GraphDatabaseService db) {
        this.graphDb = db;
        this.traversalProvider = new Neo4jTraversalProvider(db);
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).

        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                TransactionManager.shutdown();
                graphDb.shutdown();
            }
        } );
    }

    @Override
    public void create(String path) {
        File dbf = new File(path);
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbf);
        traversalProvider = new Neo4jTraversalProvider(graphDb);
        registerShutdownHook(graphDb);
    }

    @Override
    public TraversalProvider getTraversalProvider() {
        return this.traversalProvider;
    }

    @Override
    public void transaction() {
        TransactionManager.getTransaction(graphDb);
    }

    @Override
    public void commit() {
        TransactionManager.commit(graphDb);
    }

    public Element createElement(String index, int id, Callback<Element> callback){
        transaction();
        Neo4jEmbeddedDatabase db = this;
        UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDb, index) {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty("id", properties.get("id"));
                new Neo4jElement(db, created).update(callback);
            }
        };

        return new Neo4jElement(db, factory.getOrCreate("id", id));
    }

    public Stream<Neo4jElement> getAllNodes() {
        return graphDb.getAllNodes().stream().map(n -> new Neo4jElement(this, n));
    }

    @Override
    public Stream<Element> findElements(Label label, PropertyValuePair property) {
        return graphDb
                .findNodes(org.neo4j.graphdb.Label.label(label.name()), property.property.name(), property.value)
                .stream()
                .map(n -> new Neo4jElement(this, n));
    }
}
