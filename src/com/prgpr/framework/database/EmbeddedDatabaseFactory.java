package com.prgpr.framework.database;

import com.prgpr.framework.database.neo4j.Neo4jEmbeddedDatabase;
import com.prgpr.framework.database.neo4j.Neo4jTransactionFactory;
import com.prgpr.framework.database.transaction.BatchTransactionManager;
import com.prgpr.framework.database.transaction.SimpleTransactionManager;
import com.prgpr.framework.database.transaction.TransactionManager;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Created by kito on 19.11.16.
 */
public class EmbeddedDatabaseFactory {
    public static EmbeddedDatabase newEmbeddedDatabase(String path){
        return new Neo4jEmbeddedDatabase(path);
    }

    public static EmbeddedDatabase newEmbeddedDatabase(String path, int transactionBatchSize){
        Neo4jEmbeddedDatabase db = new Neo4jEmbeddedDatabase(path);
        TransactionManager tm =new BatchTransactionManager(db.getTransactionManager(), transactionBatchSize);
        db.setTransactionManager(tm);
        return db;
    }

    public static EmbeddedDatabase newEmbeddedDatabase(GraphDatabaseService db) {
        return new Neo4jEmbeddedDatabase(db);
    }
}
