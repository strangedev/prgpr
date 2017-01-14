package com.prgpr.framework.database.transaction;

import java.util.concurrent.Callable;

/**
 * Created by kito on 08.12.16.
 *
 * Manages the transactions of the database
 *
 * @author Kyle Rinfreschi
 */
public interface TransactionManager {
    /**
     *
     * @param callable Run code block in the current transaction
     * @param <T> the type to be returned by the callable
     * @return  the code block to be executed
     */
    <T> T execute(Callable<T> callable);

    /**
     * The transaction was successful
     */
    void success();

    /**
     * The transaction was not successful
     */
    void failure();

    /**
     * Close all open transactions
     */
    void closeOpenTransactions();

    /**
     * @return the current batch size as int
     */
    int getBatchSize();

    /**
     * Set the current batch size. A value of -1 disables batching
     * @param batchSize the transaction batch size
     */
    void setBatchSize(int batchSize);
}
