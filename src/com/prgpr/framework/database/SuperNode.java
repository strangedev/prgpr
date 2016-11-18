package com.prgpr.framework.database;

import com.prgpr.data.Page;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.UniqueFactory;

import java.util.Map;

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
        node.addLabel(label);
    }

    public Object getProperty(Property property){
        return node.getProperty(property.name());
    }

    public < E > void setProperty(Property property, E val){
        node.setProperty(property.name(), val);
    }

    public static SuperNode getOrCreate(GraphDatabaseService graphDb, String index, int hashcode, NodeCallable callable){
        UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDb, index) {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty("id", properties.get("id"));
                new SuperNode(created).update(callable);
            }
        };

        return new SuperNode(factory.getOrCreate("id", hashcode));
    }

    public void update(NodeCallable run){
        try {
            run.call(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(){
        TransactionManager.commit(graphDb);
    }
}
