package com.prgpr.framework.database.transaction;

import java.util.concurrent.Callable;

/**
 * Created by kito on 08.12.16.
 */
public interface TransactionManager {
    <T> T execute(Callable<T> callable);

    void execute(Runnable runnable);

    void success();

    void failure();

    void shutdown();

    boolean inTransaction();
}
