package com.prgpr.framework.database.neo4j;

import com.prgpr.framework.database.Callback;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.Label;
import com.prgpr.framework.database.Property;
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

    public void addLabel(Label label){
        db.transaction(()-> node.addLabel(org.neo4j.graphdb.Label.label(label.name())));
    }

    public Stream<Label> getLabels() {
        return db.transaction(() ->
                StreamSupport.stream(node.getLabels().spliterator(), false)
                    .map(label -> (Label) label::name));
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

    public enum RelTypes implements org.neo4j.graphdb.RelationshipType {
        categoryLink,  // defines the Relationship category
        articleLink
    }

    @Override
    public Relationship createRelationshipTo(Element incoming, RelTypes relation) {
        return node.createRelationshipTo(((Neo4jElement)incoming).getNode(), relation);
    }

}
