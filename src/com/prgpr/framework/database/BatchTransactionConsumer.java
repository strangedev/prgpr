package com.prgpr.framework.database;

import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.consumer.Consumer;
import com.prgpr.framework.consumer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by strange on 12/7/16.
 * @author Noah Hummel
 */
public class BatchTransactionConsumer implements Consumer<Long> {

    private static final Logger log = LogManager.getFormatterLogger(BatchTransactionConsumer.class);
    private EmbeddedDatabase db;
    private final long batchSize;
    private long received = 0;

    public BatchTransactionConsumer(EmbeddedDatabase db, long batchSize) {
        this.db = db;
        this.batchSize = batchSize;
    }

    @Override
    public void consume(Long c) {
        this.received += c;

        if (this.received >= this.batchSize){
            log.info("Committing batch (size: "+ received +") to database.");
            try {
                this.db.success();
            } catch (NotInTransactionException e) {
                log.catching(e);
            }
            this.received = 0;
        }
    }

    @Override
    public void onUnsubscribed(Producer<Long> producer) {
        log.info("Committing batch (size: "+ received +") to database.");
        try {
            this.db.success();
        } catch (NotInTransactionException e) {
            log.catching(e);
        }
    }
}
