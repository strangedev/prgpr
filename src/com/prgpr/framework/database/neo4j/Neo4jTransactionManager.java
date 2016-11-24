package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.DatabaseShutdownException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.NotInTransactionException;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by kito on 18.11.16.
 */
class Neo4jTransactionManager {
    private static final Logger log = LogManager.getFormatterLogger(Neo4jTransactionManager.class);

    private static final HashMap<GraphDatabaseService, Transaction> transactions = new HashMap<>();

    static Transaction getTransaction(GraphDatabaseService db){
        Transaction tx = transactions.get(db);

        if(tx == null){
            try {
                tx = db.beginTx();
                transactions.put(db, tx);
            }catch (DatabaseShutdownException e){
                log.error(e.getStackTrace());
            }
        }

        return tx;
    }

    static void getTransaction(GraphDatabaseService db, Runnable runnable){
        try{
            Neo4jTransactionManager.getTransaction(db);
            runnable.run();
        } catch (Exception e){
            log.error(e);
        }
    }

    static <T> T getTransaction(GraphDatabaseService db, Callable<T> callable){
        try{
            Neo4jTransactionManager.getTransaction(db);
            return callable.call();
        } catch (Exception e){
            log.error(e);
        }

        return null;
    }

    static void shutdown(){
        transactions.forEach((db, tx) -> {
            try {
                if (tx != null) {
                    tx.close();
                }
            }catch (NotInTransactionException e){
                // do nothing
            } catch (Exception e){
                log.error(e);
            }
        });
    }

    static void success(GraphDatabaseService db){
        commit(db, Transaction::success);
    }

    static void failure(GraphDatabaseService db){
        commit(db, Transaction::failure);
    }

    private static void commit(GraphDatabaseService db, Callback<Transaction> cb) {
        try (Transaction tx = transactions.get(db)) {

            if(tx == null){
                throw new NotInTransactionException();
            }

            cb.call(tx);
        } catch (Exception e) {
            log.error(e);
        } finally {
            transactions.put(db, null);
        }
    }
}
