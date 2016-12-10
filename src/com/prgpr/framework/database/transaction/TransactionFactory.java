package com.prgpr.framework.database.transaction;

/**
 * @author Kyle Rinfreschi
 * Created by kito on 08.12.16.
 */
public interface TransactionFactory {
    /**
     * @return a newly created Transaction instance
     */
    Transaction createTransaction();
}
