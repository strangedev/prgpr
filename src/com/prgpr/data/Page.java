package com.prgpr.data;

import com.prgpr.PageFactory;
import com.prgpr.framework.database.*;

import com.prgpr.framework.database.neo4j.RelationshipTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static com.prgpr.LinkExtraction.extractArticles;
import static com.prgpr.LinkExtraction.extractCategories;

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

    public Set<Page> getCategories() {
        return SearchProvider.findAnyImmediateIncoming(
                this.node,
                WikiNamespaces.PageLabel.Category,
                RelationshipTypes.categoryLink,
                (PropertyValuePair) null)
                .stream()
                .map(e -> new Page(e)).collect(Collectors.toCollection(LinkedHashSet::new));
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

    public void insertCategoryLinks() {
        addRelationships(WikiNamespaces.PageLabel.Category, RelationshipTypes.categoryLink, () -> extractCategories(this.getHtml()));
    }

    public void insertArticleLinks() {
        addRelationships(WikiNamespaces.PageLabel.Article, RelationshipTypes.articleLink, () -> extractArticles(this.getHtml()));
    }

    
    private void addRelationships(WikiNamespaces.PageLabel label, RelationshipType relType, Callable<Set<String>> callable) {
        Set<String> titles;

        try {
            titles = callable.call();
        } catch (Exception e) {
            log.error(e);
            return;
        }

        int namespace = WikiNamespaces.fromPageLabel(label);

        String pageTitle = this.getTitle();

        titles.forEach((title) -> {
            long hash = hashCode(namespace, title);

            Element elem = node.getDatabase().getNodeFromIndex(indexName, hash);

            if(elem == null) {
                return;
            }

            node.createUniqueRelationshipTo(elem, relType); //@TODO: make the relation directed

            log.info("A relation from " + pageTitle + " to article " + title + " was created.");
        });
    }


}
