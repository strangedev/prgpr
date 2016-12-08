package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.transaction.Transaction;
import com.prgpr.framework.database.transaction.TransactionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.DatabaseShutdownException;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Created by kito on 08.12.16.
 */
public class Neo4jTransactionFactory implements TransactionFactory {
    private static final Logger log = LogManager.getFormatterLogger(Neo4jTransactionFactory.class);
    private final GraphDatabaseService db;

    Neo4jTransactionFactory(GraphDatabaseService db){
        this.db = db;
    }

    @Override
    public Transaction createTransaction(){
        try {
            return new Neo4jTransaction(this.db.beginTx());
        }catch (DatabaseShutdownException e){
            log.catching(e);
        }
        return null;
    }
}
