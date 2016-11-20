package com.prgpr.framework.database;

import com.prgpr.data.Page;
import com.sun.org.apache.xpath.internal.functions.Function;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.UniqueFactory;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by kito on 11/17/16.
 */
public class SuperNode {

    private static GraphDatabaseService graphDb;

    private Node node;

    public SuperNode(Node node){
        this.node = node;
        graphDb = node.getGraphDatabase();
    }

    public void addLabel(Label label){
        TransactionManager.getTransaction(graphDb);
        node.addLabel(label);
    }

    public Object getProperty(Property property){
        TransactionManager.getTransaction(graphDb);
        return node.getProperty(property.name());
    }

    public <E> void setProperty(Property property, E val){
        TransactionManager.getTransaction(graphDb);
        node.setProperty(property.name(), val);
    }

    //@TODO: return another Type
    public < E > Node findNode(Label label, Property property, E val){
        TransactionManager.getTransaction(graphDb);
        //@TODO: implement
        return null;
    }


    public static SuperNode getOrCreate(GraphDatabaseService graphDb, String index, int hashcode, Callback<SuperNode> callback){
        TransactionManager.getTransaction(graphDb);
        UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDb, index) {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty("id", properties.get("id"));
                new SuperNode(created).update(callback);
            }
        };

        return new SuperNode(factory.getOrCreate("id", hashcode));
    }

    public void update(Callback<SuperNode> callback){
        try {
            TransactionManager.getTransaction(graphDb);
            callback.call(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(){
        TransactionManager.commit(graphDb);
    }
}
