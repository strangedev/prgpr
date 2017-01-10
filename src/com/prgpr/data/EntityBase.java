package com.prgpr.data;


import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.RelationshipTypes;
import com.prgpr.framework.database.SearchProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Elizaveta Kovalevskaya
 *
 * An abstract Class, building the Base of the enteties.
 */
public abstract class EntityBase {

    private static final Logger log = LogManager.getFormatterLogger(EntityBase.class);
    private static final String stringHashFunction = "SHA-1";


    public abstract String getTitle();

    public abstract long getHashCode();

    public abstract int hashCode();

    /**
     * Calculates the hash.
     *
     * http://eclipsesource.com/blogs/2012/09/04/the-3-things-you-should-know-about-hashcode/
     * http://preshing.com/20110504/hash-collision-probabilities/
     * http://stackoverflow.com/questions/1867191/probability-of-sha1-collisions
     *
     * @param title of the page
     * @return the hash
     */
    protected static int hashCode(String title, int namespaceID) {
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
     * @param o Object to compare to
     * @return boolean of the comparison
     */
    public abstract boolean equals(Object o);

    /**
     * Searching for the Page, which is the source of the Wikidata
     *
     * @param node Element (from the subclass)
     * @return the source Page
     */
    protected Page getSource(Element node) {
        Set<Page> source = new LinkedHashSet<>();
        Iterator<Page> iterator = source.iterator();

        source = SearchProvider.findImmediateOutgoing(
                node,
                RelationshipTypes.sourceLink
        )
                .stream()
                .map(Page::new)
                .collect(Collectors.toSet());
        return source.iterator().next();
    }

    /**
     * Inserts the sourceLink from the Element to the Page with his Wikidata.
     *
     * @param node Element (from the subclass)
     * @param page Source Page
     * @return The title of the source
     */
    protected String insertSourceLink(Element node, Page page) {
        node.getDatabase().transaction(() -> {
            Element elem = node.getDatabase().getNodeFromIndex("Pages", page.getHashCode());
            if(elem != null) { node.createUniqueRelationshipTo(elem, RelationshipTypes.sourceLink); }
        });

        return page.getTitle();
    }

    public abstract Stream<String> insertEntityLinks();

    /**
     * Get's all the Elements to create the entity relation to and inserts it into the database.
     *
     * @param node Element (from the subclass)
     * @param source Source Page
     * @return The titles of all entities, where links were inserted.
     */
    protected Stream<String> insertEntityLinks(Element node, Page source) {

        Set<String> related = new LinkedHashSet<>();

        Set<Page> elements = source.getLinkedArticles(); // get's all linked articles of the page
        node.getDatabase().transaction(() -> {
            for (Page page : elements) {

                // get's all the Persons, Cities and Monuments of the Page, which were created based on their source.
                Set<Element> equivalentNode = page.getSourcing();

                for (Element elem : equivalentNode) {

                    if (elem == null) {
                        continue;
                    }

                    node.createUniqueRelationshipTo(elem, RelationshipTypes.entityLink);
                }

                related.add(page.getTitle());
            }
        });

        return related.stream();
    }
}
