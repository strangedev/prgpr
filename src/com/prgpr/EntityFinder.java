package com.prgpr;

import com.prgpr.data.*;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.SearchProvider;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by strange on 1/13/17.
 * @author Noah Hummel
 *
 * Like PageFinder, but for Entities!
 * Allow searching for entities (Person, City, Monument) in the database, but returns instances of EntityBase
 * to proxy the database foo and stuff.
 * You've read the spec, you know what's up.
 */
public class EntityFinder {

    private static EmbeddedDatabase db;

    /**
     * A function to set the database as the used by this instance.
     *
     * @param graphDb An embedded database which is used at runtime
     */
    public static void setDatabase(EmbeddedDatabase graphDb){
        EntityFinder.db = graphDb;
    }

    /**
     *
     * @return
     */
    public static Stream<EntityBase> getAll() {
        return Stream.concat(getAllPersons(), Stream.concat(getAllCities(), getAllMonuments()));
    }

    /**
     * Returns all person entities in the DB as a stream.
     * @return A stream of all known person entities
     */
    public static Stream<Person> getAllPersons() {
        return SearchProvider.findAnyWithLabel(EntityFinder.db, EntityTypes.Person, null, null).stream().map(Person::new);
    }

    /**
     * Returns all city entities in the DB as a stream.
     * @return A stream of all known city entities
     */
    public static Stream<City> getAllCities() {
        return SearchProvider.findAnyWithLabel(EntityFinder.db, EntityTypes.City, null, null).stream().map(City::new);
    }

    /**
     * Returns all monument entities in the DB as a stream.
     * @return A stream of all known monument entities
     */
    public static Stream<Monument> getAllMonuments() {
        return SearchProvider.findAnyWithLabel(EntityFinder.db, EntityTypes.Monument, null, null).stream().map(Monument::new);
    }

    /**
     * Returns a Person entity by it's wikidata entity id if found. Returns null otherwise.
     * @param entityId The wikidata entity id
     * @return An instance of Person, if the associated person was found, null otherwise.
     */
    public static Person getPersonByEnitityId(String entityId) {
        Element node = SearchProvider.findNode(EntityFinder.db, EntityTypes.Person, EntityBase.EntityAttribute.entityId, entityId);
        if (node != null) return new Person(node);
        return null;
    }

    /**
     * Returns a City entity by it's wikidata entity id if found. Returns null otherwise.
     * @param entityId The wikidata entity id
     * @return An instance of City, if the associated city was found, null otherwise.
     */
    public static City getCityByEnitityId(String entityId) {
        Element node = SearchProvider.findNode(EntityFinder.db, EntityTypes.City, EntityBase.EntityAttribute.entityId, entityId);
        if (node != null) return new City(node);
        return null;
    }

    /**
     * Returns a Monument entity by it's wikidata entity id if found. Returns null otherwise.
     * @param entityId The wikidata entity id
     * @return An instance of Monument, if the associated monument was found, null otherwise.
     */
    public static Monument getMonumentByEnitityId(String entityId) {
        Element node = SearchProvider.findNode(EntityFinder.db, EntityTypes.Monument, EntityBase.EntityAttribute.entityId, entityId);
        if (node != null) return new Monument(node);
        return null;
    }

    /**
     * Gets all known entities with the given title.
     * @param pageTitle The page title
     * @return All known entities with this title
     */
    public static Set<EntityBase> getEntitiesByPageTitle(String pageTitle) {
        Set<EntityBase> matches = new LinkedHashSet<>();
        EntityFinder.getAllPersons()
                .filter(p -> p.getTitle().equalsIgnoreCase(pageTitle))
                .forEach(matches::add);
        EntityFinder.getAllCities()
                .filter(c -> c.getTitle().equalsIgnoreCase(pageTitle))
                .forEach(matches::add);
        EntityFinder.getAllMonuments()
                .filter(m -> m.getTitle().equalsIgnoreCase(pageTitle))
                .forEach(matches::add);
        return matches;
    }

    /**
     * Gets the Person entity with the given title or null.
     * @param pageTitle The page title
     * @return The Person entity with the given title or null.
     */
    public static Person getPersonByPageTitle(String pageTitle) {
        Set<EntityBase> matches = EntityFinder.getEntitiesByPageTitle(pageTitle);
        return matches.stream()
                .filter(e -> e instanceof Person)
                .map(e -> (Person)e)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the City entity with the given title or null.
     * @param pageTitle The page title
     * @return The City entity with the given title or null.
     */
    public static City getCityByPageTitle(String pageTitle) {
        Set<EntityBase> matches = EntityFinder.getEntitiesByPageTitle(pageTitle);
        return matches.stream()
                .filter(e -> e instanceof City)
                .map(e -> (City)e)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the Monument entity with the given title or null.
     * @param pageTitle The page title
     * @return The Monument entity with the given title or null.
     */
    public static Monument getMonumentByPageTitle(String pageTitle) {
        Set<EntityBase> matches = EntityFinder.getEntitiesByPageTitle(pageTitle);
        return matches.stream()
                .filter(e -> e instanceof Monument)
                .map(e -> (Monument)e)
                .findFirst()
                .orElse(null);
    }

    /**
     *
     * @param databaseId
     * @return
     */
    public static EntityBase findByDatabaseId(String databaseId) {
        long id = -1;
        try {
            id = Long.parseLong(databaseId);
        } catch (Exception e) {
            return null;
        }
        Element e = db.getNodeById(id);
        if (e.getLabels().anyMatch(l -> l == EntityTypes.Entity)) {
            if (e.getLabels().anyMatch(l -> l ==EntityTypes.City)) return new Person(e);
            if (e.getLabels().anyMatch(l -> l ==EntityTypes.City)) return new City(e);
            if (e.getLabels().anyMatch(l -> l ==EntityTypes.City)) return new Monument(e);
        }
        return null;
    }

}
