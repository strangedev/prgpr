package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.data.WikiNamespaces;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.Label;
import com.prgpr.framework.database.SearchProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Noah Hummel
 * Created by strange on 12/2/16.
 */
public class PageFinder {

    private static final Logger log = LogManager.getFormatterLogger(PageFinder.class);

    private static EmbeddedDatabase db;

    public static void setDatabase(EmbeddedDatabase graphDb){
        PageFinder.db = graphDb;
    }

    public Page findByNamespaceAndTitle(int namespaceID, String title) {

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

    public Set<Page> findAllByNamespace(int namespaceID) {

        Set<Page> ret = new LinkedHashSet<>();

        Set<Element> es = SearchProvider.findAnyWithLabel(
                db,
                WikiNamespaces.fromID(namespaceID),
                null
        );

        if (!es.isEmpty()) {
            es.stream()
                    .map(PageFactory::getPage);
        }

        return ret;
    }

}
