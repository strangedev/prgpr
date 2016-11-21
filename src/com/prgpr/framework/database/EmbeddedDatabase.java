package com.prgpr.framework.database;

import com.prgpr.framework.database.neo4j.Neo4jElement;

import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 */
public interface EmbeddedDatabase {
    void create(String path);
    void transaction();
    void commit();
    Element createElement(String index, int id,  Callback<Element> callback);
    Stream<Neo4jElement> getAllNodes();
    Stream<Element> findElements(Label label, PropertyValuePair property);
}
