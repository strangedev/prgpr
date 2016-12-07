package com.prgpr.framework.database;

import com.prgpr.data.Page;
import com.prgpr.framework.consumer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by strange on 12/7/16.
 */
public class BatchTransactionProducer extends Producer<Long> {

    private static final Logger log = LogManager.getFormatterLogger(BatchTransactionProducer.class);
    private Stream<Callable<Long>> generators;

    @Override
    public void run() {
        this.generators.forEach(g -> {
            try {
                this.emit(g.call());
            } catch (Exception e) {
                log.catching(e);
                log.info("A batch transaction could not be performed.");
            }
        });
    }

    public BatchTransactionProducer(Stream<Callable<Long>> generators) {
        this.generators = generators;
    }
}
