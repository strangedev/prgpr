package com.prgpr.framework.database.transaction;

/**
 * Created by kito on 08.12.16.
 */
public interface TransactionFactory {
    /**
     * @return create a new Transaction instance
     */
    Transaction createTransaction();
}
