package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.*;
import org.neo4j.graphdb.*;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by kito on 11/17/16.
 *
 * Implementation of Element for Neo4J.
 *
 * @author Kyle Rinfreschi
 * Docstrings are kept to a minimum, since all overridden methods are described in
 * detail in the superclass.
 */
public class Neo4jElement implements Element {

    private Neo4jEmbeddedDatabase db;
    private Node node;  // keep a reference to the neo4j node

    /**
     * Constructor. Neo4jElements cannot be created without passing a neo4j node to them,
     * which guarantees the Elements existence in the db.
     *
     * @param db The neo4j database this Element exists in.
     * @param node The neo4j node this Element relates to.
     */
    Neo4jElement(Neo4jEmbeddedDatabase db, Node node){
        this.db = db;
        this.node = node;
    }

    /**
     * Getter method for the neo4j node.
     * @return The neo4j node associated to this Element.
     */
    Node getNode() {
        return node;
    }

    @Override
    public void addLabel(com.prgpr.framework.database.Label label){
        db.transaction(() -> {
            node.addLabel(org.neo4j.graphdb.Label.label(label.name()));
            return null;
        });
    }

    @Override
    public Stream<com.prgpr.framework.database.Label> getLabels() {
        return db.transaction(() ->
                StreamSupport.stream(node.getLabels().spliterator(), false)
                    .map(label -> (com.prgpr.framework.database.Label) label::name));
    }

    @Override
    public Object getProperty(Property property){
        return db.transaction(() -> node.getProperty(property.name()));
    }

    @Override
    public <E> void setProperty(Property property, E val){
        db.transaction(() -> {
            node.setProperty(property.name(), val);
            return null;
        });
    }

    @Override
    public void update(Callback<Element> callback) {
        db.transaction(() -> {
            callback.call(this);
            return null;
        });
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

    @Override
    public String getId() {
        return String.valueOf(node.getId());
    }

}
