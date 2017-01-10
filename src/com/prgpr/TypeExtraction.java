package com.prgpr;

import com.prgpr.data.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Elizaveta Kovalevskaya
 *
 * A Class to extract the type of a Page, either it is a person, a city or a monument.
 * Maybe both.
 */
public class TypeExtraction {

    private static final Logger log = LogManager.getFormatterLogger(TypeExtraction.class);

    /**
     * A method filtering the categories of a page, so that only the relevant are left.     *
     *
     * @param page The page to discover
     * @return A Set of possible types, what the content of the page could be.
     */
    public static Set<Page> discoverTypes(Page page) {

        Set<Page> categories = new LinkedHashSet<>();
        categories = page.getAllRelatedCategories();

        Set<Page> entityTypes = categories
                .stream()
                .filter(cat -> cat.getTitle().equals("Person")
                                        || cat.getTitle().equals("Ort")
                                        || cat.getTitle().equals("Denkmal"))
                 .collect(Collectors.toSet());

        // Logging the type of a Page
        entityTypes.forEach(cat ->
        {   switch(cat.getTitle()) {
                case "Person":
                    log.info(page.getTitle() + " is a person.");
                case "Ort":
                    log.info(page.getTitle() + " is a city.");
                case "Denkmal":
                    log.info(page.getTitle() + " is a monument.");
                default: break;
            }
        });

        return entityTypes;

    }

}