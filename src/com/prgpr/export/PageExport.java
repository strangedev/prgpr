package com.prgpr.export;

import com.prgpr.data.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

/**
 *
 * An Export Class for creating an output file in xml format
 * and appending data to it in batches.
 *
 * @author Kyle Rinfreschi, Noah Hummel
 */
public class PageExport {

    private static final Logger log = LogManager.getFormatterLogger(PageExport.class);

    public static Element makePagesRoot(SimpleXmlDocument document) {
        return document.createElement("pages");
    }

    public static void appendPage(Page page, SimpleXmlDocument document, Element parent) {
        if (parent == null) return;

        Element pageRoot = makePageRoot(page, document);
        parent.appendChild(pageRoot);

        Element categoriesRoot = makeCategoriesRoot(document);
        pageRoot.appendChild(categoriesRoot);

        page.getCategories().forEach(category -> {
            appendCategory(category, document, categoriesRoot);
        });
    }

    private static Element makePageRoot(Page page, SimpleXmlDocument document) {
        Element pageRoot = document.createElement("page");
        pageRoot.setAttribute("id", page.getId());
        pageRoot.setAttribute("pageId", String.valueOf(page.getArticleID()));
        pageRoot.setAttribute("namespaceId", String.valueOf(page.getNamespaceID()));
        pageRoot.setAttribute("title", page.getTitle());
        return pageRoot;
    }

    private static Element makeCategoriesRoot(SimpleXmlDocument document) {
        return document.createElement("categories");
    }

    private static void appendCategory(Page categoryPage, SimpleXmlDocument document, Element parent) {
        Element category = document.createElement("category");
        category.setAttribute("name", categoryPage.getTitle());
        parent.appendChild(category);
    }

}
