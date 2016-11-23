package com.prgpr.framework.database;

import com.prgpr.framework.database.neo4j.Neo4jElement;
import org.neo4j.graphdb.Relationship;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by kito on 21.11.16.
 */
public interface Element {

    void addLabel(Label label);

    Stream<Label> getLabels();

    Object getProperty(Property property);

    <E> void setProperty(Property property, E val);

    //<E> Element findNode(Label label, Property property, E val);

    Relationship createRelationshipTo(Element ingoing, Neo4jElement.RelTypes relation);

    void update(Callback<Element> callback);

    EmbeddedDatabase getDatabase();

}
