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
        id,
        articleID,
        namespaceID,
        title,
        html
    }

    private Page(GraphDatabaseService graphDb, Node node) {
        this.graphDb = graphDb;
        this.node = new SuperNode(node);
    }

    public Page(GraphDatabaseService graphDb, long id, int namespaceID, String title) {
        this.graphDb = graphDb;
        this.node = new SuperNode(getOrCreate(graphDb, id, namespaceID, title));
    }

    public int getID() {
        return (int)node.getPropertyAtomic(PageAttribute.id);
    }

    public long getArticleID() {
        return (long)node.getPropertyAtomic(PageAttribute.articleID);
    }

    public int getNamespaceID(){
        return (int)node.getPropertyAtomic(PageAttribute.namespaceID);
    }

    public String getTitle(){
        return (String)node.getPropertyAtomic(PageAttribute.title);
    }

    public void setHtml(String html){
        node.setPropertyAtomic(PageAttribute.html, html);
    }

    public void setHtml(StringBuilder html){
        setHtml(html.toString());
    }

    public String getHtml(){
        return (String)node.getPropertyAtomic(PageAttribute.html);
    }

    private static Node getOrCreate(GraphDatabaseService graphDb, long id, int namespaceID, String title){

        try ( Transaction tx = graphDb.beginTx() ) {
            UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(graphDb, "Pages") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    SuperNode node = new SuperNode(created);
                    node.getNode().addLabel(Label.label("Page"));
                    node.setProperty(PageAttribute.id, properties.get(PageAttribute.id.name()));
                    node.setProperty(PageAttribute.articleID, id);
                    node.setProperty(PageAttribute.namespaceID, namespaceID);
                    node.setProperty(PageAttribute.title, title);
                }
            };

            Node node = factory.getOrCreate(PageAttribute.id.name(), hashCode(namespaceID, title));
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

        if (getID() != page.getID()) return false;
        if (getNamespaceID() != page.getNamespaceID()) return false;
        return getTitle() != null ? getTitle().equals(page.getTitle()) : page.getTitle() == null;

    }

    @Override
    public int hashCode() {
        return hashCode(getNamespaceID(), getTitle());
    }
}
