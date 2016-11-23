package com.prgpr.framework.database.neo4j;


import com.prgpr.data.Page;
import com.prgpr.framework.database.*;
import com.prgpr.framework.database.Label;
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

    public Node getNode() {
        return node;
    }

    public void addLabel(Label label){
        db.transaction();
        node.addLabel(org.neo4j.graphdb.Label.label(label.name()));
    }

    public Stream<Label> getLabels() {
        return StreamSupport.stream(node.getLabels().spliterator(), false)
                .map(label -> new Label() {
                    @Override
                    public String name() {
                        return label.name();
                    }
                });
    }

    public Object getProperty(Property property){
        db.transaction();
        return node.getProperty(property.name());
    }

    public <E> void setProperty(Property property, E val){
        db.transaction();
        node.setProperty(property.name(), val);
    }

    @Override
    public void update(Callback<Element> callback) {
        try {
            db.transaction();
            callback.call(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Neo4jEmbeddedDatabase getDatabase() {
        return db;
    }

    public enum RelTypes implements org.neo4j.graphdb.RelationshipType {
        categoryLink,  // defines the Relationship category
        articleLink
    }

    @Override
    public Relationship createRelationshipTo(Element ingoing, RelTypes relation) {
        return node.createRelationshipTo(((Neo4jElement)ingoing).getNode(), relation);
    }

}
