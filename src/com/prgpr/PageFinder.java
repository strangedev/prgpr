package com.prgpr;

import com.prgpr.data.EntityTypes;
import com.prgpr.data.Page;
import com.prgpr.data.WikiNamespaces;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.SearchProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * A Class (as the name implies) to find Pages in the database.
 *
 * @author Noah Hummel
 */
public class PageFinder {

    private static final Logger log = LogManager.getFormatterLogger(PageFinder.class);
    private static EmbeddedDatabase db;

    /**
     * A function to set the database as the used by this instance.
     *
     * @param graphDb An embedded database which is used at runtime
     */
    public static void setDatabase(EmbeddedDatabase graphDb){
        PageFinder.db = graphDb;
    }

    /**
     * Searches for Page by given namespaceID and title.
     *
     * @param namespaceID of the Page to find
     * @param title of the Page to find
     * @return the Node of the Page in the database
     */
    public static Page findByNamespaceAndTitle(int namespaceID, String title) {

        Element e = SearchProvider.findNode(
                db,
                WikiNamespaces.fromID(namespaceID),
                Page.PageAttribute.title,
                title
        );

        if (e != null) {
            return PageFactory.getPage(e);
        }

        return null;

    }

    /**
     * Searches for all Pages from one namespace.
     *
     * @param namespaceID of the Pages to find
     * @return A Set of all Page-Objects from given namespace
     */
    public static Set<Page> findAllByNamespace(int namespaceID) {

        Set<Page> ret = new LinkedHashSet<>();

        Set<Element> es = SearchProvider.findAnyWithLabel(
                db,
                WikiNamespaces.fromID(namespaceID),
                null,
                null
        );

        if (!es.isEmpty()) {
            ret = es.stream().map(PageFactory::getPage).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return ret;
    }

    /**
     * Searches for all Pages with a given title
     *
     * @param pageTitle of the Pages to find
     * @return A Set of all Page-Objects with given title
     */
    public static Set<Page> findAllByTitle(String pageTitle) {

        Set<Page> ret = new LinkedHashSet<>();

        Set<Element> es = SearchProvider.findAnyWithLabel(
                db,
                EntityTypes.Page,
                Page.PageAttribute.title,
                pageTitle
        );

        if (!es.isEmpty()) {
            ret = es.stream().map(PageFactory::getPage).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return ret;
    }

    /**
     *
     * @return
     */
    public static Set<Page> getAll() {
        Set<Page> ret = new LinkedHashSet<>();

        Set<Element> es = SearchProvider.findAnyWithLabel(
                db,
                EntityTypes.Page,
                null,
                null
        );

        if (!es.isEmpty()) {
            ret = es.stream().map(PageFactory::getPage).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return ret;
    }

    /**
     *
     * @param databaseId
     * @return
     */
    public static Page findByDatabaseId(String databaseId) {
        long id = -1;
        try {
            id = Long.parseLong(databaseId);
        } catch (Exception e) {
            return null;
        }
        Element e = db.getNodeById(id);
        if (e.getLabels().anyMatch(l -> l == EntityTypes.Page)) return new Page(e);
        return null;
    }
}
