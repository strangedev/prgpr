package com.prgpr.data;


import com.prgpr.metadata.wikidata.Wikidata;
import com.prgpr.PageFactory;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.Property;
import com.prgpr.framework.database.RelationshipTypes;
import com.prgpr.framework.database.SearchProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
    protected int ownNamespaceID;
    protected Element node;
    protected Element source;

    public enum EntityAttribute implements Property {
        hash,
        title,
        ownNamespaceID,
        entityId
        }

    @Override
    public int hashCode() { return hashCode(getTitle(), ownNamespaceID); }

    /**
     * @return the hashcode of the Person
     */
    public long getHashCode() { return (long)node.getProperty(EntityAttribute.hash); }

    /**
     * @return the title of the Person's article from Wikipedia (the name of the Person)
     */
    public String getTitle() { return (String)node.getProperty(EntityAttribute.title); }


    /**
     * Calculates the hash.
     *
     * http://eclipsesource.com/blogs/2012/09/04/the-3-things-you-should-know-about-hashcode/
     * http://preshing.com/20110504/hash-collision-probabilities/
     * http://stackoverflow.com/questions/1867191/probability-of-sha1-collisions
     *
     * @param title of the page
     * @param namespaceID the namespace id of the entity
     * @return the hash
     */
    public static int hashCode(String title, int namespaceID) {
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
     * @return the source Page
     */
    public Page getSource() { return PageFactory.getPage(source); }


    /**
     * Gets entities linked by the entity.
     *
     * @return Entity objects the entity links to.
     * So the Entity of the articles the source page links to.
     */
    public Set<EntityBase> getLinkedEntities() {
        return SearchProvider.findImmediateOutgoing(
                this.node,
                RelationshipTypes.entityLink
        )
                .stream()
                .map((linkedEntities) -> {
                    if ((int)linkedEntities.getProperty(EntityAttribute.ownNamespaceID) == 16) { return new Person(linkedEntities); }
                    if ((int)linkedEntities.getProperty(EntityAttribute.ownNamespaceID) == 17) { return new City(linkedEntities); }
                    if ((int)linkedEntities.getProperty(EntityAttribute.ownNamespaceID) == 18) { return new Monument(linkedEntities); }
                    else { return null; }
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Gets entities linking the entity.
     *
     * @return Entity objects the entity is linked by.
     * So the Entity of the articles the source page is linked by.
     */
    public Set<EntityBase> getLinkingEntities() {
        return SearchProvider.findImmediateIncoming(
                this.node,
                RelationshipTypes.entityLink
        )
                .stream()
                .map((linkedEntities) -> {
                    if ((int)linkedEntities.getProperty(EntityAttribute.ownNamespaceID) == 16) { return new Person(linkedEntities); }
                    if ((int)linkedEntities.getProperty(EntityAttribute.ownNamespaceID) == 17) { return new City(linkedEntities); }
                    if ((int)linkedEntities.getProperty(EntityAttribute.ownNamespaceID) == 18) { return new Monument(linkedEntities); }
                    else { return null; }
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Inserts the sourceLink from the Element to the Page with his Wikidata.
     *
     * @return The title of the source
     */
    protected String insertSourceLink() {
        node.getDatabase().transaction(() -> {
            Element elem = node.getDatabase().getNodeFromIndex("Pages", getSource().getHashCode());
            if(elem != null) {
                node.createUniqueRelationshipTo(elem, RelationshipTypes.sourceLink);
            }

            return null;
        });

        return getSource().getTitle();
    }

    /**
     * Get's all the Elements to create the entity relation to and inserts it into the database.
     *
     * @return The titles of all entities, where links were inserted, linking Persons, Cities and Monuments.
     */
    public Stream<String> insertEntityLinks() {

        Set<String> related = new LinkedHashSet<>();

        Set<Page> elements = getSource().getLinkedArticles(); // get's all linked articles of the page
        node.getDatabase().transaction(() -> {
            for (Page page : elements) {

                // get's all the Persons, Cities and Monuments of the Page, which were created based on their source.
                Set<Element> equivalentNode = page.getSourcing();

                for (Element elem : equivalentNode) {

                    if (elem == null) {
                        continue;
                    }

                    node.createUniqueRelationshipTo(elem, RelationshipTypes.entityLink);

                    related.add(page.getTitle());
                }
            }

            return null;
        });

        return related.stream();
    }

    /**
     * Inserts the entity id as a property of the node
     */
    public void insertEntityId() {
        this.node.setProperty(EntityAttribute.entityId, Wikidata.getEntityId(this.getTitle(), this.getSource().getNamespaceID()));
    }

    public String getEntityId() {
        Object result = this.node.getProperty(EntityAttribute.entityId);
        return result != null? (String)result : "NOT_FOUND";
    }

    @Override
    public abstract String toString();

}
