package com.prgpr.framework.database;

import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.database.transaction.TransactionFactory;
import com.prgpr.framework.database.transaction.TransactionManager;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 *
 * An abstract embedded database.
 * Provides an interface for the database logic to depend on so that the actual database
 * backend is interchangeable.
 *
 * @author Kyle Rinfreschi
 */
public interface EmbeddedDatabase {

    /**
     * Creates a transaction if not already created before calling the callback
     * @param runnable a Runnable / lambda callback
     */
    void transaction(Runnable runnable);

    /**
     * Creates a transaction if not already created before calling the callback
     * @param callable  a Callable / lambda callback
     * @param <T> the type to be returned by the callable
     * @return the type defined by T
     */
    <T> T transaction(Callable<T> callable);

    /**
     * Mark the current transaction as successful
     * @throws NotInTransactionException should not be called outside a transaction
     */
    void success() throws NotInTransactionException;

    /**
     * Mark the current transaction as failed
     * @throws NotInTransactionException should not be called outside a transaction
     */
    void failure() throws NotInTransactionException;

    /**
     *
     * @param index the name of the index to be used
     * @param id the id / hash to be used to identify the element
     * @param callback the callback to add other properties / labels to the element
     * @return a newly created Element
     */
    Element createElement(String index, long id, Callback<Element> callback);

    /**
     * @return a stream of elements
     */
    Stream<Element> getAllElements();

    /**
     * Find a node in an index given its hash
     * @param indexName the name of the index used for the lookup
     * @param hash the id / hash of the element
     * @return the found element or null if not found
     */
    Element getNodeFromIndex(String indexName, long hash);

    /**
     * Find all elements given their label and property value
     * @param label the label used for filtering
     * @param property the property value pair used for filtering
     * @return a stream of elements
     */
    Stream<Element> findElements(Label label, PropertyValuePair property);

    /**
     * @return an instance of TraversalProvider
     */
    TraversalProvider getTraversalProvider();

    /**
     * @return an instance of TransactionFactory
     */
    TransactionFactory getTransactionFactory();

    /**
     * @return an instance of TransactionManager
     */
    TransactionManager getTransactionManager();

    /**
     * Sets the transaction manager
     * @param tm the transaction manager to be used
     */
    void setTransactionManager(TransactionManager tm);

    void shutdown();
}
