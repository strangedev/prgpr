package com.prgpr.framework.database.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by kito on 08.12.16.
 */
public class BatchTransactionManager implements TransactionManager {
    private static final Logger log = LogManager.getFormatterLogger(BatchTransactionManager.class);
    private final TransactionManager tm;
    private final int batchSize;
    private int accumulator = 0;

    public BatchTransactionManager(TransactionManager tm, int batchSize){
        this.tm = tm;
        this.batchSize = batchSize;
    }

    @Override
    public <T> T execute(Callable<T> callable) {
        T result = null;
        try {
            result = tm.execute(callable);
            accumulate();
        }catch (Exception e){
            log.catching(e);
            failure();
        }

        return result;
    }

    @Override
    public void execute(Runnable runnable) {
        tm.execute(runnable);
        accumulate();
    }

    @Override
    public void success() {
        accumulator = 0;
        tm.success();
    }

    @Override
    public void failure() {
        accumulator = 0;
        tm.failure();
    }

    @Override
    public void shutdown(){
        if(accumulator > 0) {
            success();
        }
        tm.shutdown();
    }

    @Override
    public boolean inTransaction() {
        return tm.inTransaction();
    }

    private void accumulate() {
        if(tm.inTransaction()) {
            accumulator++;
            batchCommit();
        }
    }

    private void batchCommit(){
        if(accumulator % batchSize == 0){
            success();
        }
    }
}