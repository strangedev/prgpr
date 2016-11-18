package com.prgpr.data;

import com.prgpr.framework.database.Property;
import com.prgpr.framework.database.SuperNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;

/**
 * @author Kyle Rinfreschi
 *
 * Page Class.
 * It represents a wikipedia page.
 */
public class Page {

    private static final Logger log = LogManager.getFormatterLogger(Page.class);
    private SuperNode node;

    public enum PageLabel implements Label {
        Page
    }

    public enum PageAttribute implements Property
    {
        id,
        articleID,
        namespaceID,
        title,
        html
    }

    public Page(GraphDatabaseService graphDb, long id, int namespaceID, String title, String html) {
        this.node = SuperNode.getOrCreate(graphDb, "Pages", hashCode(namespaceID, title), (node) -> {
            node.addLabel(PageLabel.Page);
            node.setProperty(PageAttribute.articleID, id);
            node.setProperty(PageAttribute.namespaceID, namespaceID);
            node.setProperty(PageAttribute.title, title);
            node.setProperty(PageAttribute.html, html);
        });
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
        SuperNode.save();
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
