package com.prgpr;

import java.util.Set;

public class Page {
    private int namespaceID;
    private String title;
    private Set<String> categories;

    /**
     * @param namespaceID
     * @param title
     */
    public Page(int namespaceID, String title) {
        this.namespaceID = namespaceID;
        this.title = title;
    }

    /**
     * @param namespaceID
     * @param title
     * @param categories
     */
    public Page(int namespaceID, String title, Set<String> categories) {
        this.namespaceID = namespaceID;
        this.title = title;
        this.categories = categories;
    }

    /**
     * @return int
     */
    public int getNamespaceID() {
        return namespaceID;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Set<String>
     */
    public Set<String> getCategories() {
        return categories;
    }

    /**
     * @param categories
     */
    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    /**
     *
     * @param Object o
     * @return bool
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return namespaceID == page.namespaceID && (title != null ? title.equals(page.title) : page.title == null);
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return (31 * namespaceID) + ((title != null) ? title.hashCode() : 0);
    }
}
