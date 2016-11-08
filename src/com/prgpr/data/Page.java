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

    private final long id;
    private final int namespaceID;
    private final String title;
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
