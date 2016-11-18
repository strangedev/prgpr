package com.prgpr.framework.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;

/**
 * Created by kito on 18.11.16.
 */
public class TransactionManager {
    private static final Logger log = LogManager.getFormatterLogger(SuperNode.class);

    private static final HashMap<GraphDatabaseService, Transaction> transactions = new HashMap<>();

    public static void registerDb(GraphDatabaseService db){
        Transaction tx = transactions.get(db);

        if(tx == null){
            transactions.put(db, db.beginTx());
        }
    }

    public static void commit(GraphDatabaseService db){
        try (Transaction tx = transactions.get(db)) {
            tx.success();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            transactions.put(db, db.beginTx());
        }
    }

    public static void shutdown(){
        transactions.forEach((db, tx)-> tx.close());
    }
}
