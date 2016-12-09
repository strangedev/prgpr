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

    /**
     * @param n The Element to decide on.
     * @param labels A set of labels to check against.
     * @return Whether any of the Element's labels matche any of a set of given labels.
     */
    public static boolean matchesAnyLabel(Element n, Set<Label> labels){
        return labels == null ||  // allows the user to pass null as well, automatically matches
                n.getLabels()
                .anyMatch(labels::contains)
                || labels.isEmpty();  // allows the user to pass empty sets as well, automatically matches
    }

    /**
     * @param n The Element to decide on.
     * @param labels A set of labels to check against.
     * @return Whether all of the Element's labels match any of a set of given labels.
     */
    public static boolean matchesAllLabels(Element n, Set<Label> labels){
        return labels == null ||
                n.getLabels()
                .allMatch(labels::contains)
                || labels.isEmpty();
    }

    /**
     * @param n The Element to decide on.
     * @param label A label to check against.
     * @return Whether any of the Element's labels matches the given label.
     */
    public static boolean matchesLabel(Element n, Label label){
        return label == null ||
                n.getLabels()
                .anyMatch(l -> l.name().equals(label.name()))
                || label.name().isEmpty();
    }

    /**
     * @param n The Element to decide on.
     * @param properties A set of properties to check against.
     * @return Whether all of the given properties match the Element's properties.
     */
    public static boolean matchesAllProperties(Element n, Set<PropertyValuePair> properties){
        return properties == null ||
                properties.stream()
                .allMatch(p ->
                        n.getProperty(p.property).equals(p.value)
                ) || properties.isEmpty();
    }

    /**
     * @param n The Element to decide on.
     * @param properties A set of properties to check against.
     * @return Whether any of the given properties match the Element's properties.
     */
    public static boolean matchesAnyProperties(Element n, Set<PropertyValuePair> properties){
        return properties == null ||
                properties.stream()
                .anyMatch(p ->
                        n.getProperty(p.property).equals(p.value)
                )
                || properties.isEmpty();
    }

    /**
     * @param n The Element to decide on.
     * @param property A property to check against.
     * @return Whether given property matches the Element's property.
     */
    public static boolean matchesProperty(Element n, PropertyValuePair property){
        return property == null || n.getProperty(property.property) == property.value;
    }
    
}
