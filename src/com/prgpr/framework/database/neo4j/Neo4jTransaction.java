package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.transaction.Transaction;

/**
 * @author Kyle Rinfreschi
 * Created by kito on 08.12.16.
 *
 * A wrapper class for the neo4j transaction
 */
class Neo4jTransaction implements Transaction {
    private org.neo4j.graphdb.Transaction tx;

    Neo4jTransaction(org.neo4j.graphdb.Transaction tx){
        this.tx = tx;
    }

    /**
     * The transaction was successful
     */
    @Override
    public void success() {
        tx.success();
    }

    /**
     * The transaction was not successful
     */
    @Override
    public void failure() {
        tx.failure();
    }

    /**
     * Close the transaction
     */
    @Override
    public void close() {
        tx.close();
    }
}
