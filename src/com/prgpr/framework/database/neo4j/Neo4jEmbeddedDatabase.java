package com.prgpr.framework.database.neo4j;

import com.prgpr.data.Page;
import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.database.*;
import com.prgpr.framework.database.transaction.SimpleTransactionManager;
import com.prgpr.framework.database.transaction.TransactionFactory;
import com.prgpr.framework.database.transaction.TransactionManager;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.index.UniqueFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * @author kyle Rinfreschi
 * Created by kito on 21.11.16.
 *
 * Docstrings are kept to a minimum, since all overridden methods are described in
 * detail in the superclass.
 */
public class Neo4jEmbeddedDatabase implements EmbeddedDatabase {

    private Neo4jTraversalProvider traversalProvider;
    private Neo4jTransactionFactory transactionFactory;
    private TransactionManager transactionManager;

    private GraphDatabaseService graphDb;
    private static final String idIndex = "hash";

    public Neo4jEmbeddedDatabase(GraphDatabaseService db) {
        this.graphDb = db;
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb, final TransactionManager tm)
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).

        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                tm.shutdown();
                graphDb.shutdown();
            }
        } );
    }

    public Neo4jEmbeddedDatabase(String path) {
        File dbf = new File(path);
        this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbf);
        this.traversalProvider = new Neo4jTraversalProvider(graphDb);
        this.transactionFactory = new Neo4jTransactionFactory(graphDb);
        this.transactionManager = new SimpleTransactionManager(this.transactionFactory);
        registerShutdownHook(graphDb, transactionManager);
    }

    @Override
    public TraversalProvider getTraversalProvider() {
        return transaction(() -> this.traversalProvider );

    }

    @Override
    public TransactionFactory getTransactionFactory() {
        return this.transactionFactory;
    }

    @Override
    public TransactionManager getTransactionManager(){
        return this.transactionManager;
    }

    @Override
    public void setTransactionManager(TransactionManager tm) {
        this.transactionManager = tm;
    }

    @Override
    public void transaction(Runnable runnable)
    {
        transactionManager.execute(runnable);
    }

    @Override
    public <T> T transaction(Callable<T> callable)
    {
        return transactionManager.execute(callable);
    }

    @Override
    public void success() throws NotInTransactionException {
        transactionManager.success();
    }

    @Override
    public void failure() throws NotInTransactionException {
        transactionManager.failure();
    }

    @Override
    public Element createElement(String index, long hash, Callback<Element> callback){
        Neo4jEmbeddedDatabase db = this;
        return transaction(() -> {
            UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDb, index) {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty(idIndex, hash);
                    new Neo4jElement(db, created).update(callback);
                }
            };

            return new Neo4jElement(db, factory.getOrCreate(idIndex, hash));
        });
    }

    @Override
    public Stream<Element> getAllElements() {
        return transaction(() -> graphDb.getAllNodes()
                                            .stream()
                                            .map(n -> new Neo4jElement(this, n)));
    }

    public Relationship createUniqueRelationshipTo(Element start, Element end, com.prgpr.framework.database.RelationshipType relType) {
        long hash = relationshipHash(start, end, relType);
        return transaction(() -> {
            UniqueFactory<Relationship> factory = new UniqueFactory.UniqueRelationshipFactory(graphDb, relType.name()) {
                @Override
                protected Relationship create(Map<String, Object> properties) {
                    Relationship r = ((Neo4jElement) start)
                            .getNode()
                            .createRelationshipTo(
                                    ((Neo4jElement) end).getNode(),
                                    org.neo4j.graphdb.RelationshipType.withName(relType.name())
                            );
                    r.setProperty(idIndex, hash);
                    return r;
                }
            };

            return factory.getOrCreate(idIndex, hash);
        });
    }

    private int relationshipHash(Element start, Element end, RelationshipType relType) {

        long startId = (long) start.getProperty(Page.PageAttribute.hash);
        long endId = (long) end.getProperty(Page.PageAttribute.hash);
        long relHash = relType.hashCode();

        int result = (int) (startId ^ (startId >>> 32));
        result += 31 * result + (int)(endId ^ (endId >>> 32));
        result += 31 * result + (int)(relHash ^ (relHash >>> 32));
        return result;
    }

    @Override
    public Element getNodeFromIndex(String indexName, long id) {
        return transaction(() -> {
            ReadableIndex<Node> index = graphDb.index().forNodes(indexName);
            Node node = index.get(idIndex, id).getSingle();
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
