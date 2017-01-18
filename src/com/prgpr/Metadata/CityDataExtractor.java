package com.prgpr.Metadata;

import com.eclipsesource.json.JsonObject;
import com.prgpr.Metadata.WikidataApi.SnakExtractor;
import com.prgpr.Metadata.WikidataApi.Wikidata;
import com.prgpr.data.City;

/**
 * Created by strange on 1/13/17.
 * @author Noah Hummel
 *
 * A class wrapping data extraction for City entities
 * neatly.
 */
public class CityDataExtractor {

    public static String extractName(City city) {
        return city.getTitle();
    }

    public static String extractCountry(City city) {
        JsonObject snak = Wikidata.getSnak(city.getEntityId(), "P17");
        String countryEntityId = "";
        if (snak != null) countryEntityId = SnakExtractor.parseItemEntityId(snak);
        if (countryEntityId.isEmpty()) return "";
        return Wikidata.getPageTitle(countryEntityId);
    }

    public static String extractPopulation(City city) {
        JsonObject snak = Wikidata.getSnak(city.getEntityId(), "P1082");
        String populationSize = "";
        if (snak != null) populationSize = SnakExtractor.parseQuantityAmount(snak);
        return populationSize;
    }

    public static String extractEarliestMention(City city) {
        JsonObject snak = Wikidata.getSnak(city.getEntityId(), "P1249");
        String earliestMention = "";
        if (snak != null) earliestMention = SnakExtractor.parsePointInTime(snak);
        return earliestMention;
    }
}
