package com.prgpr.framework.database.transaction;

/**
 * Created by kito on 08.12.16.
 */
public interface Transaction  extends AutoCloseable {
    void success();
    void failure();
}
