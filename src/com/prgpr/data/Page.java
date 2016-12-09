package com.prgpr.data;

import com.prgpr.framework.database.*;

import com.prgpr.framework.database.RelationshipTypes;
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
    private String t = "";

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

    /**
     * Gets the ID of the Page object (calculated by hashCode()).
     *
     * @return page hash
     */
    public long getHashCode() {
        return (long)node.getProperty(PageAttribute.hash);
    }

    /**
     * Gets the ID of the Page object of the actual article
     *
     * @return page arctileID
     */
    public long getArticleID() {
        return (long)node.getProperty(PageAttribute.articleID);
    }

    /**
     * Gets the namespaceID of the Page object
     *
     * @return page namespace
     */
    public int getNamespaceID(){
        return (int)node.getProperty(PageAttribute.namespaceID);
    }

    /**
     * Gets the title of the Page object
     *
     * @return page tite
     */
    public String getTitle(){
        return (String)node.getProperty(PageAttribute.title);
    }

    /**
     * Sets the html-content to the Page object
     *
     * @param html page html-content
     */
    public void setHtml(String html){
        node.setProperty(PageAttribute.html, html);
    }

    /**
     * Sets the html-content to the Page object by calling the setHtml function with a String
     *
     * @param html page html-content
     */
    public void setHtml(StringBuilder html){
        setHtml(html.toString());
    }

    /**
     * Gets the html content of the Page object casted to a String
     *
     * @return page html content as a String
     */
    public String getHtml(){
        return (String)node.getProperty(PageAttribute.html);
    }

    /**
     * Gets the immediate categories of the page
     *
     * @return A Set with all immediate categories of the page
     */
    public Set<Page> getCategories() {
        return SearchProvider.findImmediateOutgoing(
                this.node,
                RelationshipTypes.categoryLink
        )
                .stream()
                .map(Page::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Gets the all related categories of the page
     *
     * @return A Set with all categories of the page
     */
    public Set<Page> getAllRelatedCategories() {
        return SearchProvider.findAllInSubgraph(
                this.node,
                RelationshipTypes.categoryLink
                )
                .stream()
                .map(Page::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Gets articles linked by the page.
     *
     * @return Articles the page links to
     */
    public Set<Page> getLinkedArticles() {
        return SearchProvider.findImmediateOutgoing(
                this.node,
                RelationshipTypes.articleLink
        )
                .stream()
                .map(Page::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Gets articles linking to the page.
     *
     * @return Articles linking to the page
     */
    public Set<Page> getLinkingArticles() {
        return SearchProvider.findImmediateIncoming(
                this.node,
                RelationshipTypes.articleLink
        )
                .stream()
                .map(Page::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * A function to calculate a unique hash to find out fast if the Node/Page already exists.
     *
     * @return the hash of the Page
     */
    @Override
    public int hashCode() {
        return hashCode(getNamespaceID(), getTitle());
    }

    /**
     * Calculates the hash.
     *
     * http://eclipsesource.com/blogs/2012/09/04/the-3-things-you-should-know-about-hashcode/
     * http://preshing.com/20110504/hash-collision-probabilities/
     * http://stackoverflow.com/questions/1867191/probability-of-sha1-collisions
     *
     * @param namespaceID of the page
     * @param title of the page
     * @return the hash
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

        return (namespaceID * 31) + Arrays.hashCode(titleHash);
    }

    /**
     * Compares Page object to another Object
     *
     * @param o other Object to compare to
     * @return if the objects are equal as a bool
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        if (getHashCode() != page.getHashCode()) return false;
        if (getNamespaceID() != page.getNamespaceID()) return false;
        return getTitle() != null ? getTitle().equals(page.getTitle()) : page.getTitle() == null;

    }

    /**
     * A function which calls addRelationship() to insert category relationships into the database
     */
    public long insertCategoryLinks() {
        return addRelationships(WikiNamespaces.PageLabel.Category, RelationshipTypes.categoryLink, () -> extractCategories(this.getHtml()));
    }

    /**
     * A function which calls addRelationship() to insert article relationships into the database
     */
    public long insertArticleLinks() {
        this.t = getTitle(); // TODO remove
        return addRelationships(WikiNamespaces.PageLabel.Article, RelationshipTypes.articleLink, () -> extractArticles(this.getHtml()));
    }

    /**
     * A function inserting links into the database.
     *
     * @param label Type of the Page
     * @param relType Type of the relation to be inserted
     * @param callable a function to extract the Pages to create the relation to
     */
    private long addRelationships(WikiNamespaces.PageLabel label, RelationshipType relType, Callable<Set<String>> callable) {
        Set<String> titles;
        long relationshipsAdded = 0;

        try {
            titles = callable.call();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }

        int namespace = WikiNamespaces.fromPageLabel(label);
        String pageTitle = this.getTitle();
        log.info("------------------------------------");
        log.info("Creating relationships of " + label + " for " + pageTitle);
        log.info("------------------------------------");

        for(String title: titles) {
            long hash = hashCode(namespace, title);
            Element elem = node.getDatabase().getNodeFromIndex(indexName, hash);

            if(elem == null) {
                log.info("Skipping " + label + " relationship to " + title);
                continue;
            }

            node.createUniqueRelationshipTo(elem, relType);

            relationshipsAdded++;
            log.info("A relation from " + pageTitle + " to " + label + " " + title + " was created.");

        }

        return relationshipsAdded;
    }


}
