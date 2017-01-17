package com.prgpr.framework.database.transaction;

/**
 * Created by kito on 08.12.16.
 *
 * A transaction resource wrapper
 *
 * @author Kyle Rinfreschi
 */
public interface Transaction  extends AutoCloseable {
    /**
     * The transaction was successful
     */
    void success();

    /**
     * The transaction was not failureful
     */
    void failure();
}
