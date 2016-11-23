package com.prgpr.data;

import com.prgpr.framework.database.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Relationship;
import com.prgpr.framework.database.neo4j.Neo4jElement;

import java.util.Set;

import static com.prgpr.LinkExtraction.extractArticles;
import static com.prgpr.LinkExtraction.extractCategories;
import static com.prgpr.framework.database.neo4j.Neo4jElement.RelTypes.articleLink;
import static com.prgpr.framework.database.neo4j.Neo4jElement.RelTypes.categoryLink;

/**
 * @author Kyle Rinfreschi
 *
 * Page Class.
 * It represents a wikipedia page.
 */
public class Page {


    private static final Logger log = LogManager.getFormatterLogger(Page.class);
    private static final String indexName = "Pages";
    private Element node;

    /**
     * Contains all Wikipedia namespaces.
     * Page is a generic label.
     */

    public enum PageAttribute implements Property
    {
        id,
        articleID,
        namespaceID,
        title,
        html
    }


    public Page(Element node) {
        this.node = node;
    }

    public Page(EmbeddedDatabase db, long id, int namespaceID, String title, String html) {
        this.node = db.createElement(indexName, hashCode(namespaceID, title), (node) -> {
            node.addLabel(WikiNamespaces.PageLabel.Page);
            node.addLabel(WikiNamespaces.fromID(namespaceID));
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

    //@TODO: implement
    public void insertCategoryLink() {
        Set<String> categoryTitles = extractCategories(this.getHtml());
        String pageTitle = this.getTitle();

        int categoryNamespace = WikiNamespaces.fromPageLabel(WikiNamespaces.PageLabel.Category);

        for (String title : categoryTitles) {
            long hash = hashCode(categoryNamespace, title);

            Element category = node.getDatabase().getNodeFromIndex(indexName, hash);

            if(category == null) {
                continue;
            }

            node.createRelationshipTo(category, categoryLink);
            log.info("A relation from " + pageTitle + " to category " + title + " was created.");
        }
    }

    public void insertArticleLink() {
        Set<String> articleTitles = extractArticles(this.getHtml());
        String pageTitle = this.getTitle();

        int articleNamespace = WikiNamespaces.fromPageLabel(WikiNamespaces.PageLabel.Article);

        for (String title : articleTitles) {
            long hash = hashCode(articleNamespace, title);

            Element article = node.getDatabase().getNodeFromIndex(indexName, hash);
            if(article == null) {
                continue;
            }

            node.createRelationshipTo(article, articleLink); //@TODO: make the relation directed
            log.info("A relation from " + pageTitle + " to article " + title + " was created.");
        }
    }


}
