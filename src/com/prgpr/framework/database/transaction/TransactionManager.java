package com.prgpr.framework.database.transaction;

import java.util.concurrent.Callable;

/**
 * Created by kito on 08.12.16.
 */
public interface TransactionManager {
    /**
     *
     * @param callable Run code block in the current transaction
     * @return  the code block to be executed
     */
    <T> T execute(Callable<T> callable);

    /**
     * Run code block in the current transaction
     * @param runnable the code block to be executed
     */
    void execute(Runnable runnable);

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
    void shutdown();

    boolean inTransaction();
}
