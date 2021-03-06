package com.prgpr.framework.database;

/**
 * Created by strange on 11/21/16.
 *
 * An interface which provides abstraction for RelationshipTypes for various graph databases.
 *
 * @author Noah Hummel
 */
public interface RelationshipType {

    /**
     * @return The name of the RelationshipType.
     */
    String name();
}
