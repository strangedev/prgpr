package com.prgpr.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Kyle Rinfreschi
 *
 * Page Class.
 * It represents a wikipedia page.
 */
public class Page {
    private long id;

    private int namespaceID;
    private String title;
    private Set<String> categories;

    /**
     * Constructor.
     * Categories will be empty.
     *
     * @param namespaceID An initial namespaceID.
     * @param title And initial title.
     */
    public Page(long id, int namespaceID, String title) {
        this.id = id;
        this.namespaceID = namespaceID;
        this.title = title;
        this.categories = new LinkedHashSet<>();
    }

    /**
     * Constructor.
     *
     * @param namespaceID An initial namespaceID.
     * @param title An initial title.
     * @param categories An initial Set of categories.
     */
    public Page(long id, int namespaceID, String title, Set<String> categories) {
        this.id = id;
        this.namespaceID = namespaceID;
        this.title = title;
        this.categories = categories;
    }

    public long getId() {
        return id;
    }

    public int getNamespaceID() {
        return namespaceID;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return namespaceID == page.namespaceID && (title != null ? title.equals(page.title) : page.title == null);
    }

    @Override
    public int hashCode() {
        return (31 * namespaceID) + ((title != null) ? title.hashCode() : 0);
    }

}
