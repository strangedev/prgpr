package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.*;
import org.neo4j.graphdb.*;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by kito on 11/17/16.
 */
public class Neo4jElement implements Element {

    private Neo4jEmbeddedDatabase db;
    private Node node;

    public Neo4jElement(Neo4jEmbeddedDatabase db, Node node){
        this.db = db;
        this.node = node;
    }

    Node getNode() {
        return node;
    }

    public void addLabel(com.prgpr.framework.database.Label label){
        db.transaction(()-> node.addLabel(org.neo4j.graphdb.Label.label(label.name())));
    }

    public Stream<com.prgpr.framework.database.Label> getLabels() {
        return db.transaction(() ->
                StreamSupport.stream(node.getLabels().spliterator(), false)
                    .map(label -> (com.prgpr.framework.database.Label) label::name));
    }

    public Object getProperty(Property property){
        return db.transaction(() -> node.getProperty(property.name()));
    }

    public <E> void setProperty(Property property, E val){
        db.transaction(() -> node.setProperty(property.name(), val));
    }

    @Override
    public void update(Callback<Element> callback) {
        db.transaction(()-> callback);
    }

    @Override
    public Neo4jEmbeddedDatabase getDatabase() {
        return db;
    }

    @Override
    public Relationship createUniqueRelationshipTo(Element incoming, com.prgpr.framework.database.RelationshipType relation) {
        //return node.createRelationshipTo(((Neo4jElement)incoming).getNode(), relation);
        return db.createUniqueRelationshipTo(this, incoming, relation);
    }

}
