package com.prgpr.framework.database;

import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.database.transaction.Transaction;
import com.prgpr.framework.database.transaction.TransactionFactory;
import com.prgpr.framework.database.transaction.TransactionManager;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 */
public interface EmbeddedDatabase {
    void transaction(Runnable runnable);
    <T> T transaction(Callable<T> callable);
    void success() throws NotInTransactionException;
    void failure() throws NotInTransactionException;
    Element createElement(String index, long id, Callback<Element> callback);
    Stream<Element> getAllElements();
    Element getNodeFromIndex(String indexName, long hash);
    Stream<Element> findElements(Label label, PropertyValuePair property);
    TraversalProvider getTraversalProvider();
    TransactionFactory getTransactionFactory();
    TransactionManager getTransactionManager();
    void setTransactionManager(TransactionManager tm);
}
