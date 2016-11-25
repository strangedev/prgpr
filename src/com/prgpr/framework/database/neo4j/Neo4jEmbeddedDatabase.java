package com.prgpr.framework.database.neo4j;

import com.prgpr.data.Page;
import com.prgpr.framework.database.*;
import com.prgpr.framework.database.Label;
import com.prgpr.framework.database.RelationshipType;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.index.UniqueFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 */
public class Neo4jEmbeddedDatabase implements EmbeddedDatabase {

    private Neo4jTraversalProvider traversalProvider;
    private GraphDatabaseService graphDb;

    Neo4jEmbeddedDatabase() {}

    Neo4jEmbeddedDatabase(GraphDatabaseService db) {
        this.graphDb = db;
        this.traversalProvider = new Neo4jTraversalProvider(db);
        registerShutdownHook(graphDb);
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
                Neo4jTransactionManager.shutdown();
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
        Neo4jTransactionManager.getTransaction(graphDb);
    }

    @Override
    public void transaction(Runnable runnable)
    {
        Neo4jTransactionManager.getTransaction(graphDb, runnable);
    }

    @Override
    public <T> T transaction(Callable<T> callable)
    {
        return Neo4jTransactionManager.getTransaction(graphDb, callable);
    }

    @Override
    public void commit() {
        Neo4jTransactionManager.success(graphDb);
    }

    public Element createElement(String index, long id, Callback<Element> callback){
        Neo4jEmbeddedDatabase db = this;
        return transaction(() -> {
            UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDb, index) {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("id", id);
                    new Neo4jElement(db, created).update(callback);
                }
            };

            return new Neo4jElement(db, factory.getOrCreate("id", id));
        });
    }

    public Stream<Element> getAllElements() {
        return transaction(() -> graphDb.getAllNodes()
                                            .stream()
                                            .map(n -> new Neo4jElement(this, n)));
    }

    public Relationship createUniqueRelationshipTo(Element start, Element end, com.prgpr.framework.database.RelationshipType relType) {
        UniqueFactory<Relationship> factory = new UniqueFactory.UniqueRelationshipFactory(graphDb, relType.name()) {
            @Override
            protected Relationship create(Map<String, Object> properties) {
                Relationship r =  ((Neo4jElement)start)
                                        .getNode()
                                        .createRelationshipTo(
                                                ((Neo4jElement)end).getNode(),
                                                org.neo4j.graphdb.RelationshipType.withName(relType.name())
                                        );
                r.setProperty("id", relationshipHash(start, end, relType));
                return r;
            }
        };

        return factory.getOrCreate("id", relationshipHash(start, end, relType));
    }

    private long relationshipHash(Element start, Element end, RelationshipType relType) {

        long startId = (int) start.getProperty(Page.PageAttribute.id);
        long endId = (int) end.getProperty(Page.PageAttribute.id);
        long relHash = (int) relType.hashCode();

        long result = startId ^ (startId >>> 32);
        result += endId ^ (endId >>> 32);
        result += relHash ^ (relHash >>> 32);
        return result;
    }

    @Override
    public Element getNodeFromIndex(String indexName, long id) {
        return transaction(() -> {
            ReadableIndex<Node> index = graphDb.index().forNodes(indexName);
            Node node = index.get("id", id).getSingle();
            if(node == null){
                return null;
            }

            return new Neo4jElement(this, node);
        });
    }

    @Override
    public Stream<Element> findElements(Label label, PropertyValuePair property) {
        return transaction(() -> graphDb
                .findNodes(org.neo4j.graphdb.Label.label(label.name()), property.property.name(), property.value)
                .stream()
                .map(n -> new Neo4jElement(this, n)));
    }
}
