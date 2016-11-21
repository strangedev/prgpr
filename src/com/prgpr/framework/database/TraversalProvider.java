package com.prgpr.framework.database;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by strange on 11/21/16.
 */
public abstract class TraversalProvider {

    protected enum Direction {
        INCOMING,
        OUTGOING
    }

    public Stream<Element> traverseOutgoingUnique(Element from, List<RelationshipType> relTypes){
        return getUniqueTraversal(from, relTypes, 1, Direction.OUTGOING);
    };
    
    public Stream<Element> traverseOutgoingUnique(Element from, RelationshipType relType){
        return getUniqueTraversal(from, Collections.singletonList(relType), 1, Direction.OUTGOING);
    };

    public Stream<Element> traverseOutgoingUnique(Element from, List<RelationshipType> relTypes, int depth){
        return getUniqueTraversal(from, relTypes, 1, Direction.OUTGOING);
    }

    public Stream<Element> traverseOutgoingUnique(Element from, RelationshipType relType, int depth){
        return getUniqueTraversal(from, Collections.singletonList(relType), depth, Direction.OUTGOING);
    };


    public Stream<Element> traverseIncomingUnique(Element from, List<RelationshipType> relTypes){
        return getUniqueTraversal(from, relTypes, 1, Direction.INCOMING);
    };

    public Stream<Element> traverseIncomingUnique(Element from, List<RelationshipType> relTypes, int depth){
        return getUniqueTraversal(from, relTypes, depth, Direction.INCOMING);
    };

    public Stream<Element> traverseIncomingUnique(Element from, RelationshipType relType){
        return getUniqueTraversal(from, Collections.singletonList(relType), 1, Direction.INCOMING);
    };

    public Stream<Element> traverseIncomingUnique(Element from, RelationshipType relType, int depth){
        return getUniqueTraversal(from, Collections.singletonList(relType), depth, Direction.INCOMING);
    };


    protected abstract Stream<Element> getUniqueTraversal(Element from, List<RelationshipType> relTypes, int depth, Direction direction);

}
