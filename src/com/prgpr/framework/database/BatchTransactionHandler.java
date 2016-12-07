package com.prgpr.framework.database;

import com.prgpr.framework.consumer.Consumer;
import com.prgpr.framework.consumer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Created by strange on 12/7/16.
 * @author Noah Hummel
 */
public class BatchTransactionHandler implements Consumer<Long> {

    private static final Logger log = LogManager.getFormatterLogger(BatchTransactionHandler.class);
    private EmbeddedDatabase db;
    private final long batchSize;
    private long received = 0;

    public BatchTransactionHandler(EmbeddedDatabase db, long batchSize) {
        this.db = db;
        this.batchSize = batchSize;
    }

    @Override
    public void consume(Long c) {
        this.received += c;

        if (this.received >= this.batchSize){
            log.info("Committing batch (size: "+ received +") to database.");
            this.db.commit();
            this.received = 0;
        }
    }

    @Override
    public void onUnsubscribed(Producer<Long> producer) {
        log.info("Committing batch (size: "+ received +") to database.");
        this.db.commit();
    }
}
