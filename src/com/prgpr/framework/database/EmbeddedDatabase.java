package com.prgpr.framework.database;

import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.database.transaction.Transaction;
import com.prgpr.framework.database.transaction.TransactionFactory;
import com.prgpr.framework.database.transaction.TransactionManager;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 * @author Kyle Rinfreschi
 *
 * An abstract embedded database.
 * Provides an interface for the database logic to depend on so that the actual database
 * backend is interchangeable.
 */
public interface EmbeddedDatabase {

    // TODO Kyle, please write some docstrings, you know the transaction management best.

    /**
     *
     * @param runnable
     */
    void transaction(Runnable runnable);

    /**
     *
     * @param callable
     * @param <T>
     * @return
     */
    <T> T transaction(Callable<T> callable);

    /**
     *
     * @throws NotInTransactionException
     */
    void success() throws NotInTransactionException;

    /**
     *
     * @throws NotInTransactionException
     */
    void failure() throws NotInTransactionException;

    /**
     *
     * @param index
     * @param id
     * @param callback
     * @return
     */
    Element createElement(String index, long id, Callback<Element> callback);

    /**
     *
     * @return
     */
    Stream<Element> getAllElements();

    /**
     *
     * @param indexName
     * @param hash
     * @return
     */
    Element getNodeFromIndex(String indexName, long hash);

    /**
     *
     * @param label
     * @param property
     * @return
     */
    Stream<Element> findElements(Label label, PropertyValuePair property);

    /**
     *
     * @return
     */
    TraversalProvider getTraversalProvider();

    /**
     *
     * @return
     */
    TransactionFactory getTransactionFactory();

    /**
     *
     * @return
     */
    TransactionManager getTransactionManager();

    /**
     *
     * @param tm
     */
    void setTransactionManager(TransactionManager tm);
}
