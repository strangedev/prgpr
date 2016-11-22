package com.prgpr.framework.database;

import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 */
public interface EmbeddedDatabase {
    void create(String path);
    void transaction();
    void commit();
    Element createElement(String index, long id, Callback<Element> callback);
    Stream<Element> getAllNodes();
    Element getNodeFromIndex(String indexName, long hash);
    Stream<Element> findElements(Label label, PropertyValuePair property);
    TraversalProvider getTraversalProvider();
}
