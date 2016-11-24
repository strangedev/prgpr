package com.prgpr.framework.database;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 */
public interface EmbeddedDatabase {
    void create(String path);
    void transaction();
    void transaction(Runnable runnable);
    <T> T transaction(Callable<T> callable);
    void commit();
    Element createElement(String index, long id, Callback<Element> callback);
    Stream<Element> getAllNodes();
    Element getNodeFromIndex(String indexName, long hash);
    Stream<Element> findElements(Label label, PropertyValuePair property);
    TraversalProvider getTraversalProvider();
}
