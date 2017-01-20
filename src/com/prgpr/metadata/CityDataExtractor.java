package com.prgpr.metadata;

import com.eclipsesource.json.JsonObject;
import com.prgpr.metadata.wikidata.SnakExtractor;
import com.prgpr.metadata.wikidata.Wikidata;
import com.prgpr.data.City;

/**
 * Created by strange on 1/13/17.
 * @author Noah Hummel
 *
 * A class wrapping data extraction for City entities
 * neatly.
 *
 */
public class CityDataExtractor {

    /**
     * Extracts the name attribute from a city.
     * @param city The Entity to extract from
     * @return The attribute value or an empty string alternatively
     */
    public static String extractName(City city) {
        return city.getTitle();
    }

    /**
     * Extracts the country attribute from a city.
     * @param city The Entity to extract from
     * @return The attribute value or an empty string alternatively
     */
    public static String extractCountry(City city) {
        JsonObject snak = Wikidata.getSnak(city.getEntityId(), "P17");
        String countryEntityId = "";
        if (snak != null) countryEntityId = SnakExtractor.parseItemEntityId(snak);
        if (countryEntityId.isEmpty()) return "";
        return Wikidata.getPageTitle(countryEntityId);
    }

    /**
     * Extracts the population attribute from a city.
     * @param city The Entity to extract from
     * @return The attribute value or an empty string alternatively
     */
    public static String extractPopulation(City city) {
        JsonObject snak = Wikidata.getSnak(city.getEntityId(), "P1082");
        String populationSize = "";
        if (snak != null) populationSize = SnakExtractor.parseQuantityAmount(snak);
        return populationSize;
    }

    /**
     * Extracts the earliest mention attribute from a city.
     * @param city The Entity to extract from
     * @return The attribute value or an empty string alternatively
     */
    public static String extractEarliestMention(City city) {
        JsonObject snak = Wikidata.getSnak(city.getEntityId(), "P1249");
        String earliestMention = "";
        if (snak != null) earliestMention = SnakExtractor.parsePointInTime(snak);
        return earliestMention;
    }
}
