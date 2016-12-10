package com.prgpr.framework.database;

import com.prgpr.framework.database.neo4j.Neo4jEmbeddedDatabase;
import com.prgpr.framework.database.transaction.BatchTransactionManager;
import com.prgpr.framework.database.transaction.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Created by kito on 19.11.16.
 *
 * @author Kyle Rinfreschi
 */
public class EmbeddedDatabaseFactory {
    private static final Logger log = LogManager.getFormatterLogger(EmbeddedDatabaseFactory.class);

    /**
     * @param path the path to the embedded database directory
     * @return an embedded database instance
     */
    public static EmbeddedDatabase newEmbeddedDatabase(String path){
        log.info("Establishing new database connection...");
        return new Neo4jEmbeddedDatabase(path);
    }

    /**
     * @param path the path to the embedded database directory
     * @param transactionBatchSize the min amount of transaction operations before a commit is called
     * @return an embedded database instance with a batch transaction manager
     */
    public static EmbeddedDatabase newEmbeddedDatabase(String path, int transactionBatchSize){
        log.info("Establishing new database connection...");
        Neo4jEmbeddedDatabase db = new Neo4jEmbeddedDatabase(path);
        TransactionManager tm =new BatchTransactionManager(db.getTransactionManager(), transactionBatchSize);
        db.setTransactionManager(tm);
        return db;
    }

    /**
     * For advanced usage only please use the other two options instead
     * @param db a neo4j GraphDatabaseService instance
     * @return a embedded database instance
     */
    public static EmbeddedDatabase newEmbeddedDatabase(GraphDatabaseService db) {
        log.info("Establishing new database connection...");
        return new Neo4jEmbeddedDatabase(db);
    }
}
