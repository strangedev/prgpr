package com.prgpr.framework.database;

import com.prgpr.framework.database.neo4j.Neo4jElement;

import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * Created by strange on 11/21/16.
 * @author Noah Hummel
 *
 * Provides basic predicates for filtering node streams.
 */
public class NodePredicates {

    public static boolean matchesAnyLabel(Element n, Set<Label> labels){
        return n.getLabels()
                .anyMatch(labels::contains)
                || labels.isEmpty();
    }

    public static boolean matchesAllLabels(Element n, Set<Label> labels){
        return n.getLabels()
                .allMatch(labels::contains)
                || labels.isEmpty();
    }

    public static boolean matchesLabel(Element n, Label label){
        return n.getLabels()
                .anyMatch(l -> l.name().equals(label.name()))
                || label.name().isEmpty();
    }

    public static boolean matchesAllProperties(Element n, Set<PropertyValuePair> properties){
        return properties.stream()
                .allMatch(p ->
                        n.getProperty(p.property).equals(p.value)
                ) || properties.isEmpty();
    }

    public static boolean matchesAnyProperties(Element n, Set<PropertyValuePair> properties){
        return properties.stream()
                .anyMatch(p ->
                        n.getProperty(p.property).equals(p.value)
                ) || properties.isEmpty();
    }

    public static boolean matchesProperty(Element n, PropertyValuePair property){
        return n.getProperty(property.property) == property.value;
    }
    
}
