package com.prgpr.data;

import com.prgpr.framework.Property;
import com.prgpr.framework.SuperNode;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;

import java.util.Map;

/**
 * @author Kyle Rinfreschi
 *
 * Page Class.
 * It represents a wikipedia page.
 */
public class Page {

    private GraphDatabaseService graphDb;
    private SuperNode node;

    public enum PageAttribute implements Property
    {
        pageID,
        namespaceID,
        title
    }

    private Page(GraphDatabaseService graphDb, Node node) {
        this.graphDb = graphDb;
        this.node = new SuperNode(node);
    }

    public Page(GraphDatabaseService graphDb, long id, int namespaceID, String title) {
        this.graphDb = graphDb;
        this.node = new SuperNode(getOrCreate(graphDb, id, namespaceID, title));
    }

    public void setId(long id) {
        node.setPropertyAtomic(PageAttribute.pageID, id);
    }

    public long getId() {
        return (long)node.getPropertyAtomic(PageAttribute.pageID);
    }

    public void setNamespaceID(int namespaceID){
        node.setPropertyAtomic(PageAttribute.namespaceID, namespaceID);
    }

    public int getNamespaceID(){
        return (int)node.getPropertyAtomic(PageAttribute.namespaceID);
    }

    public void setTitle(String title){
        node.setPropertyAtomic(PageAttribute.title, title);
    }

    public String getTitle(){
        return (String)node.getPropertyAtomic(PageAttribute.title);
    }

    private static Node getOrCreate(GraphDatabaseService graphDb, long id, int namespaceID, String title){

        try ( Transaction tx = graphDb.beginTx() ) {
            UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDb, "Pages") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    SuperNode node = new SuperNode(created);
                    node.getNode().addLabel(Label.label("Page"));
                    node.setProperty("id", properties.get("id"));
                    node.setProperty(PageAttribute.pageID, id);
                    node.setProperty(PageAttribute.namespaceID, namespaceID);
                    node.setProperty(PageAttribute.title, title);
                }
            };

            Node node = factory.getOrCreate("id", hashCode(namespaceID, title));
            tx.success();
            return node;
        }
    }

    private static int hashCode(int namespaceID, String title) {
        int result = namespaceID;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
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
