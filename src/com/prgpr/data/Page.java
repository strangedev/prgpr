package com.prgpr.data;

import com.prgpr.PageFactory;
import com.prgpr.framework.database.*;

import com.prgpr.framework.database.neo4j.RelationshipTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static com.prgpr.LinkExtraction.extractArticles;
import static com.prgpr.LinkExtraction.extractCategories;
import java.security.MessageDigest;

/**
 * @author Kyle Rinfreschi
 *
 * Page Class.
 * It represents a wikipedia page.
 */
public class Page {


    private static final Logger log = LogManager.getFormatterLogger(Page.class);
    private static final String indexName = "Pages";
    private static final String stringHashFunction = "SHA-1";
    private Element node;

    public enum PageAttribute implements Property
    {
        hash,
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

    public long getID() {
        return (long)node.getProperty(PageAttribute.hash);
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
        return SearchProvider.findImmediateIncoming(
                this.node,
                WikiNamespaces.PageLabel.Category,
                RelationshipTypes.categoryLink,
                (PropertyValuePair) null)
                .stream()
                .map(Page::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Page> getAllRelatedCategories() {
        return SearchProvider.findAllInSubgraph(
                this.node,
                WikiNamespaces.PageLabel.Category,
                RelationshipTypes.categoryLink,
                null)
                .stream()
                .map(PageFactory::getPage)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Page> getLinkedArticles() {
        return SearchProvider.findImmediateOutgoing(
                this.node,
                WikiNamespaces.PageLabel.Article,
                RelationshipTypes.articleLink,
                (PropertyValuePair) null)
                .stream()
                .map(PageFactory::getPage)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Page> getLinkingArticles() {
        return SearchProvider.findImmediateIncoming(
                this.node,
                WikiNamespaces.PageLabel.Article,
                RelationshipTypes.articleLink,
                (PropertyValuePair) null)
                .stream()
                .map(PageFactory::getPage)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * http://eclipsesource.com/blogs/2012/09/04/the-3-things-you-should-know-about-hashcode/
     * http://preshing.com/20110504/hash-collision-probabilities/
     * http://stackoverflow.com/questions/1867191/probability-of-sha1-collisions
     *
     * @param namespaceID
     * @param title
     * @return
     */
    private static int hashCode(int namespaceID, String title) {
        byte[] titleHash;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(stringHashFunction);
            messageDigest.update(title.getBytes());
            titleHash = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            titleHash = title.getBytes();
        }

        return Arrays.hashCode(new Object[] {
                namespaceID,
                titleHash
        });
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

            if(elem == null) return;

            node.createUniqueRelationshipTo(elem, relType);
            log.info("A relation from " + pageTitle + " to article " + title + " was created.");
        });
    }


}
