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

    public static Stream<Person> getAllPersons() {
        return SearchProvider.findAnyWithLabel(EntityFinder.db, EntityTypes.Person, null).stream().map(Person::new);
    }

    public static Stream<City> getAllCities() {
        return SearchProvider.findAnyWithLabel(EntityFinder.db, EntityTypes.City, null).stream().map(City::new);
    }

    public static Stream<Monument> getAllMonuments() {
        return SearchProvider.findAnyWithLabel(EntityFinder.db, EntityTypes.Monument, null).stream().map(Monument::new);
    }

    public static Person getPersonByEnitityId(String entityId) {
        Element node = SearchProvider.findNode(EntityFinder.db, EntityTypes.Person, EntityBase.EntityAttribute.entityId, entityId);
        if (node != null) return new Person(node);
        return null;
    }

    public static City getCityByEnitityId(String entityId) {
        Element node = SearchProvider.findNode(EntityFinder.db, EntityTypes.City, EntityBase.EntityAttribute.entityId, entityId);
        if (node != null) return new City(node);
        return null;
    }

    public static Monument getMonumentByEnitityId(String entityId) {
        Element node = SearchProvider.findNode(EntityFinder.db, EntityTypes.Monument, EntityBase.EntityAttribute.entityId, entityId);
        if (node != null) return new Monument(node);
        return null;
    }

    public static Set<EntityBase> getEntityByPageTitle(String pageTitle) {
        Set<EntityBase> matches = new LinkedHashSet<>();
        EntityFinder.getAllPersons().filter(p -> p.getTitle().equalsIgnoreCase(pageTitle)).forEach(matches::add);
        EntityFinder.getAllCities().filter(c -> c.getTitle().equalsIgnoreCase(pageTitle)).forEach(matches::add);
        EntityFinder.getAllMonuments().filter(m -> m.getTitle().equalsIgnoreCase(pageTitle)).forEach(matches::add);
        return matches;
    }

}
