package com.prgpr.data;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * @author Kyle Rinfreschi
 *
 * Page Class.
 * It represents a wikipedia page.
 */
public class Page {

    private GraphDatabaseService graphDb;
    private Node node;

    private enum Property
    {
        pageID,
        namespaceID,
        title
    }

    private Page(GraphDatabaseService graphDb, Node node) {
        this.graphDb = graphDb;
        this.node = node;
    }

    public Page(GraphDatabaseService graphDb, long id, int namespaceID, String title) {
        this.graphDb = graphDb;
        getOrCreate(this, id, namespaceID, title);
    }

    public void setId(long id) {
        setPropertyAtomic(Property.pageID, id);
    }

    public long getId() {
        return (long)getPropertyAtomic(Property.pageID);
    }

    public void setNamespaceID(int namespaceID){
        setPropertyAtomic(Property.namespaceID, namespaceID);
    }

    public int getNamespaceID(){
        return (int)getPropertyAtomic(Property.namespaceID);
    }

    public void setTitle(String title){
        setPropertyAtomic(Property.title, title);
    }

    public String getTitle(){
        return (String)getPropertyAtomic(Property.title);
    }

    private Object getProperty(Property property){
        return node.getProperty(property.name());
    }

    private < E > void setProperty(Property property, E val){
        node.setProperty(property.name(), val);
    }

    private Object getPropertyAtomic(Property property){
        Object ret;
        try ( Transaction tx = graphDb.beginTx() ) {
            ret = node.getProperty(property.name());
            tx.success();
        }
        return ret;
    }

    private < E > void setPropertyAtomic(Property property, E val){
        try ( Transaction tx = graphDb.beginTx() ) {
            node.setProperty(property.name(), val);
            tx.success();
        }
    }

    private static void getOrCreate(Page page, long id, int namespaceID, String title){
        try ( Transaction tx = page.graphDb.beginTx() )
        {
            page.node = page.graphDb.createNode(Label.label("Page"));
            page.setProperty(Property.pageID, id);
            page.setProperty(Property.namespaceID, namespaceID);
            page.setProperty(Property.title, title);
            tx.success();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        if (getId() != page.getId()) return false;
        if (getNamespaceID() != page.getNamespaceID()) return false;
        return getTitle() != null ? getTitle().equals(page.getTitle()) : page.getTitle() == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getNamespaceID();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        return result;
    }
}
