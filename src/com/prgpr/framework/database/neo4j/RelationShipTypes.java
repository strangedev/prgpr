package com.prgpr.framework.database.neo4j;

/**
 * Created by strange on 11/25/16.
 */
public enum RelationShipTypes implements org.neo4j.graphdb.RelationshipType {
    categoryLink,  // defines the Relationship category
    articleLink
}