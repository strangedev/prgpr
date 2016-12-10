package com.prgpr.framework.database.transaction;

/**
 * @author Kyle Rinfreschi
 * Created by kito on 08.12.16.
 */
public interface Transaction  extends AutoCloseable {
    /**
     * The transaction was successful
     */
    void success();

    /**
     * The transaction was not successful
     */
    void failure();
}
