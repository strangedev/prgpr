package com.prgpr.metadata;

import com.eclipsesource.json.JsonObject;
import com.prgpr.EntityFinder;
import com.prgpr.metadata.wikidata.SnakExtractor;
import com.prgpr.metadata.wikidata.Wikidata;
import com.prgpr.data.City;
import com.prgpr.data.Monument;
import com.prgpr.data.Person;

/**
 * Created by strange on 1/13/17.
 * @author Noah Hummel
 *
 * A class wrapping data extraction for Monument entities
 * neatly.
 */
public class MonumentDataExtractor {

    /**
     * Extracts the name attribute from a monument.
     * @param monument The Entity to extract from
     * @return The attribute value or an empty string alternatively
     */
    public static String extractName(Monument monument) {
        return monument.getTitle();
    }

    /**
     * Extracts the ate of inauguration attribute from a monument.
     * @param monument The Entity to extract from
     * @return The attribute value or an empty string alternatively
     */
    public static String extractDateOfInauguration(Monument monument) {
        JsonObject snak = Wikidata.getSnak(monument.getEntityId(), "P793");
        String date = "";
        if (snak != null) date = SnakExtractor.parseReferencedPointInTime(snak);
        return date;
    }

    /**
     * Extracts the creation date attribute from a monument.
     * @param monument The Entity to extract from
     * @return The attribute value or an empty string alternatively
     */
    public static String extractCreationDate(Monument monument) {
        JsonObject snak = Wikidata.getSnak(monument.getEntityId(), "P571");
        String creationDate = "";
        if (snak != null) creationDate = SnakExtractor.parsePointInTime(snak);
        return creationDate;
    }

    /**
     * Extracts the nearest city attribute from a monument.
     * @param monument The Entity to extract from
     * @return The attribute value or null
     */
    public static City extractNearestCity(Monument monument) {
        JsonObject snak = Wikidata.getSnak(monument.getEntityId(), "P276");
        String referencedEntityId = "";
        if (snak != null) referencedEntityId = SnakExtractor.parseItemEntityId(snak);
        else return null;
        return EntityFinder.getCityByEnitityId(referencedEntityId);
    }

    /**
     * Extracts the commemorated person attribute from a monument.
     * @param monument The Entity to extract from
     * @return The attribute value or null
     */
    public static Person extractCommemoratedPerson(Monument monument) {
        JsonObject snak = Wikidata.getSnak(monument.getEntityId(), "P547");
        String referencedEntityId = "";
        if (snak != null) referencedEntityId = SnakExtractor.parseItemEntityId(snak);
        else return null;
        return EntityFinder.getPersonByEnitityId(referencedEntityId);
    }

}

