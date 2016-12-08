package com.prgpr.framework.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * @author Noah Hummel
 * Created by strange on 12/8/16.
 *
 * A class which runs a stream of Callables, counts the number
 * of generated database Elements/Relationships and commits them
 * to the database in batches to minimize transaction overhead.
 */
public class BatchTransaction {

    private static final Logger log = LogManager.getFormatterLogger(BatchTransaction.class);

    /**
     * Runs a batched transaction on an embedded database.
     *
     * @param graphDb The EmbeddedDatabase to commit to
     * @param batchSize The size of a batch to commit at once
     * @param txBodies A stream of callables which contain the database logic.
     */
    public static void run(EmbeddedDatabase graphDb, long batchSize, Stream<Callable<Long>> txBodies) {

        log.info("Initializing a new batch transaction with batch size " + batchSize);

        BatchTransactionProducer p = new BatchTransactionProducer(txBodies);
        BatchTransactionConsumer c = new BatchTransactionConsumer(graphDb, batchSize);
        c.subscribeTo(p);

        log.info("Starting batch transaction...");
        p.run();
        log.info("Finishing batch transaction.");
        p.done();
    }
}
