package com.prgpr.framework.database.transaction;

/**
 * Created by kito on 08.12.16.
 *
 * Creates a new Transaction
 *
 * @author Kyle Rinfreschi
 */
public interface TransactionFactory {
    /**
     * @return a newly created Transaction instance
     */
    Transaction createTransaction();
}
