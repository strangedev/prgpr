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

    private Page(GraphDatabaseService graphDb, Node node) {
        this.graphDb = graphDb;
        this.node = node;
    }

    public Page(GraphDatabaseService graphDb, long id, int namespaceID, String title) {

        this.graphDb = graphDb;
        getOrCreate(this, id, namespaceID, title);
    }

    public void setId(long id) {
        node.setProperty("id", id);
    }

    public long getId() {
        return (long)node.getProperty("id");
    }

    public void setNamespaceID(int namespaceID){
        node.setProperty("namespaceID", namespaceID);
    }

    public int getNamespaceID(){
        return (int)node.getProperty("namespaceID");
    }

    public void setTitle(String title){
        node.setProperty("title", title);
    }

    public String getTitle(){
        String ret;
        try ( Transaction tx = graphDb.beginTx() ) {
            ret = (String) node.getProperty("title");
            tx.success();
        }
        return ret;
    }

    private static void getOrCreate(Page page, long id, int namespaceID, String title){
        try ( Transaction tx = page.graphDb.beginTx() )
        {
            page.node = page.graphDb.createNode(Label.label("Page"));
            page.setId(id);
            page.setNamespaceID(namespaceID);
            page.setTitle(title);
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
