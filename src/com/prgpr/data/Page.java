package com.prgpr.data;

import com.prgpr.PageExport;
import com.prgpr.framework.Property;
import com.prgpr.framework.SuperNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger log = LogManager.getFormatterLogger(Page.class);
    private GraphDatabaseService graphDb;
    private SuperNode node;
    private static Transaction tx;

    public enum PageAttribute implements Property
    {
        id,
        articleID,
        namespaceID,
        title,
        html
    }

    public Page(GraphDatabaseService graphDb, long id, int namespaceID, String title) {
        if(tx == null){
            tx = graphDb.beginTx();
        }

        this.graphDb = graphDb;
        this.node = new SuperNode(getOrCreate(graphDb, id, namespaceID, title));
    }

    public int getID() {
        return (int)node.getProperty(PageAttribute.id);
    }

    public long getArticleID() {
        return (long)node.getProperty(PageAttribute.articleID);
    }

    public int getNamespaceID(){
        return (int)node.getProperty(PageAttribute.namespaceID);
    }

    public String getTitle(){
        return (String)node.getProperty(PageAttribute.title);
    }

    public void setHtml(String html){
        node.setProperty(PageAttribute.html, html);
    }

    public void setHtml(StringBuilder html){
        setHtml(html.toString());
    }

    public String getHtml(){
        return (String)node.getProperty(PageAttribute.html);
    }

    public static void save(){
        try {
            tx.success();
        } catch(Exception e){
            log.error(e.getMessage());
        }finally {
            tx.close();
            tx = null;
        }
    }

    private static Node getOrCreate(GraphDatabaseService graphDb, long id, int namespaceID, String title){
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

        return factory.getOrCreate(PageAttribute.id.name(), hashCode(namespaceID, title));
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
