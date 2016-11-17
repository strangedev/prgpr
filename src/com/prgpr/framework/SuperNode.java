package com.prgpr.framework;

import com.prgpr.data.Page;
import org.neo4j.graphdb.*;
import org.neo4j.kernel.impl.core.NodeProxy;

/**
 * Created by kito on 11/17/16.
 */
public class SuperNode {

    private Node node;

    public SuperNode(Node node){
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public Object getProperty(Property property){
        return node.getProperty(property.name());
    }

    public < E > void setProperty(Property property, E val){
        node.setProperty(property.name(), val);
    }

    public Object getPropertyAtomic(Property property){
        return this.getPropertyAtomic(property.name());
    }

    public  < E > void setPropertyAtomic(Property property, E val){
        this.setPropertyAtomic(property.name(), val);
    }

    public Object getPropertyAtomic(String property){
        Object ret;
        try ( Transaction tx = node.getGraphDatabase().beginTx() ) {
            ret = node.getProperty(property);
            tx.success();
        }
        return ret;
    }

    public < E > void setPropertyAtomic(String property, E val){
        try ( Transaction tx = node.getGraphDatabase().beginTx() ) {
            node.setProperty(property, val);
            tx.success();
        }
    }
}
