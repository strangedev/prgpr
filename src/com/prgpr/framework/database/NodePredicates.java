package com.prgpr.framework.database;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * Created by strange on 11/21/16.
 */
public class NodePredicates {

    public static boolean matchesAnyLabel(Node n, Set<Label> labels){
        return StreamSupport.stream(n.getLabels().spliterator(), false)
                .anyMatch(labels::contains)
                || labels.isEmpty();
    }

    public static boolean matchesAllLabels(Node n, Set<Label> labels){
        return StreamSupport.stream(n.getLabels().spliterator(), false)
                .allMatch(labels::contains)
                || labels.isEmpty();
    }

    public static boolean matchesLabel(Node n, Label label){
        return StreamSupport.stream(n.getLabels().spliterator(), false)
                .anyMatch(l -> l.name().equals(label.name()))
                || label.name().isEmpty();
    }

    public static boolean matchesAllProperties(Node n, Set<PropertyValuePair> properties){
        return properties.stream()
                .allMatch(p ->
                        n.getProperty(p.property.name()) == p.value
                ) || properties.isEmpty();
    }

    public static boolean matchesProperty(Node n, PropertyValuePair property){
        return n.getProperty(property.property.name()) == property.value;
    }
    
}
