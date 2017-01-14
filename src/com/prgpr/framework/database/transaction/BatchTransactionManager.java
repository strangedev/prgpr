package com.prgpr.framework.database.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by kito on 08.12.16.
 *
 * @author Kyle Rinfreschi
 * Wraps an instance of a TransactionManager to allow for batching
 * Docstrings are kept to a minimum, since all overridden methods are described in
 * detail in the superclass.
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
        log.debug("Committing batch transaction");
        tm.success();
    }

    @Override
    public void failure() {
        accumulator = 0;
        tm.failure();
    }

    @Override
    public void closeOpenTransactions(){
        if(accumulator > 0) {
            success();
        }
        tm.closeOpenTransactions();
    }

    @Override
    public boolean isInTransaction() {
        return tm.isInTransaction();
    }

    /**
     * only add to count if we are in a transaction
     */
    private void accumulate() {
        if(tm.isInTransaction()) {
            accumulator++;
            batchCommit();
        }
    }

    /**
     * commit if count is same as the batch size
     */
    private void batchCommit(){
        if(accumulator % batchSize == 0){
            success();
        }
    }
}
