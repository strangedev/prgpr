package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.transaction.Transaction;

/**
 * Created by kito on 08.12.16.
 */
public class Neo4jTransaction implements Transaction {
    private final org.neo4j.graphdb.Transaction tx;

    Neo4jTransaction(org.neo4j.graphdb.Transaction tx){
        this.tx = tx;
    }
    
    @Override
    public void success() {
        tx.success();
    }

    @Override
    public void failure() {
        tx.failure();
    }

    @Override
    public void close() {
        tx.close();
    }
}
