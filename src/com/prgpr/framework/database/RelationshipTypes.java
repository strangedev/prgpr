package com.prgpr.framework.database;

/**
 * @author Noah Hummel
 * Created by strange on 11/25/16.
 *
 * An enumerator listing all used relationship types.
 */
public enum RelationshipTypes implements RelationshipType {
    categoryLink,  // article links to a category page
    articleLink  // article links to another article
}