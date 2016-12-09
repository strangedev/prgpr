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

    /**
     * @return a Neo4j node
     */
    Node getNode() {
        return node;
    }

    /**
     * Assigns a label to the element.
     * @param label the label of the element
     */
    public void addLabel(com.prgpr.framework.database.Label label){
        db.transaction(()-> node.addLabel(org.neo4j.graphdb.Label.label(label.name())));
    }

    /**
     * @return a stream of current assigned labels.
     */
    public Stream<com.prgpr.framework.database.Label> getLabels() {
        return db.transaction(() ->
                StreamSupport.stream(node.getLabels().spliterator(), false)
                    .map(label -> (com.prgpr.framework.database.Label) label::name));
    }

    /**
     * @param property property of the element
     * @return the current value assigned to the requested property
     */
    public Object getProperty(Property property){
        return db.transaction(() -> node.getProperty(property.name()));
    }

    /**
     * @param <E> the template type
     * @param property the given property
     * @param val the value to be assigned to the given property
     */
    public <E> void setProperty(Property property, E val){
        db.transaction(() -> node.setProperty(property.name(), val));
    }

    @Override
    public void update(Callback<Element> callback) {
        db.transaction(()-> callback.call(this));
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
