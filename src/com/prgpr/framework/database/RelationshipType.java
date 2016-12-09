package com.prgpr.framework.database;

/**
 * @author Noah Hummel
 * Created by strange on 11/21/16.
 *
 * An interface which provides abstraction for RelationshipTypes for various graph databases.
 */
public interface RelationshipType {

    /**
     * @return The name of the RelationshipType.
     */
    String name();
}
