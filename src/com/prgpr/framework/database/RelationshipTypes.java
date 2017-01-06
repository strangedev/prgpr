package com.prgpr.framework.database;

/**
 * Created by strange on 11/25/16.
 *
 * An enumerator listing all used relationship types.
 *
 * @author Noah Hummel
 */
public enum RelationshipTypes implements RelationshipType {
    categoryLink,  // article links to a category page
    articleLink,  // article links to another article
    sourceLink, // Entity's node of source
    entityLink // copy of the articleLink if the page isn't just a page
}