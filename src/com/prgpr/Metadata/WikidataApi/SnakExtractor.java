package com.prgpr.Metadata.WikidataApi;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * Created by strange on 1/17/17.
 * @author Noah Hummel
 *
 * >> ENOUGH IS ENOUGH!
 * >> I'VE HAD IT WITH THESE MOTHER******* SNAKS
 * >> IN THIS MOTHER******* API!
 *
 * Makes extracting data from the json the Wikidata API returns bearable.
 * Sometimes I feel like it's boxes, all the way down.
 *
 * Also, my IDE complains because it thinks I misspelled "snakes"
 */
public class SnakExtractor {

    /**
     * "snaks" box their data inside a value, inside a datavalue, inside a mainsnak,
     * and all of those might be missing. This function unpacks data if it's there and
     * does some error handling.
     *
     * @param snak The "snak" containing the data in question
     * @param identifier The identifier which is used to access the data within the value dictionary
     * @return The value of the data as String
     */
    private static String extractFromMainsnak(JsonObject snak, String identifier) {
        JsonValue mainsnak = snak.get("mainsnak");
        if (mainsnak == null) return null;
        JsonValue dataValue = mainsnak.asObject().get("datavalue");
        if (dataValue == null) return null;
        JsonValue value = dataValue.asObject().get("value");
        if (value == null) return null;
        JsonValue toExtract = value.asObject().get(identifier);
        if (toExtract == null) return null;

        return toExtract.asString();
    }

    /**
     * A method for extracting data of the "item" type from "snak"s.
     * Items usually link to another entity by their id, so this is
     * what we'll be extracting here.
     *
     * @param snak The glorious "snak"
     * @return The entity id of a linked entity, if any
     */
    public static String parseItemEntityId(JsonObject snak) {
        String entityId = extractFromMainsnak(snak, "id");
        if (entityId != null) return entityId;
        return "";
    }

    /**
     * A method for extracting data of the "item" type from "snak"s.
     * Items usually link to another entity by their id, so this is
     * what we'll be extracting here.
     *
     * @param snak The glorious "snak"
     * @return The entity id of a linked entity, if any
     */
    public static String parseQuantityAmount(JsonObject snak) {
        String amount = extractFromMainsnak(snak, "id");
        if (amount != null) return amount.replaceAll("[^\\d.]", "");
        return "";
    }

    /**
     * A method for extracting data of the "item" type from "snak"s.
     * Items usually link to another entity by their id, so this is
     * what we'll be extracting here.
     *
     * @param snak The glorious "snak"
     * @return The entity id of a linked entity, if any
     */
    public static String parsePointInTime(JsonObject snak) {
        String isoTimeString = extractFromMainsnak(snak, "time");
        if (isoTimeString != null) return isoTimeString;
        return "";
    }

    /**
     * A method for extracting data of the "item" type from "snak"s.
     * Items usually link to another entity by their id, so this is
     * what we'll be extracting here.
     *
     * @param snak The glorious "snak"
     * @return The entity id of a linked entity, if any
     */
    public static String parseReferencedPointInTime(JsonObject snak) {
        String pointInTime = parsePointInTimeQualifier(snak);
        if (pointInTime != null) return pointInTime;
        return "";
    }

    /**
     * A method for extracting data of the "item" type from "snak"s.
     * Items usually link to another entity by their id, so this is
     * what we'll be extracting here.
     *
     * @param snak The glorious "snak"
     * @return The entity id of a linked entity, if any
     */
    public static String parsePointInTimeQualifier(JsonObject snak) {
        // Error handling - java style
        JsonValue maybeQualifiers = snak.get("qualifiers");
        if (maybeQualifiers == null) return null;
        JsonValue maybeDateObject = maybeQualifiers.asObject().get("P585");
        if (maybeDateObject == null) return null;
        JsonValue dateSnak = maybeDateObject.asArray().get(0).asObject();
        if (dateSnak == null) return null;
        JsonValue dataValue = dateSnak.asObject().get("datavalue");
        if (dataValue == null) return null;
        JsonValue value = dataValue.asObject().get("value");
        if (value == null) return null;
        JsonValue timestamp = value.asObject().get("time");
        if (timestamp == null) return null;

        return timestamp.asString();
    }

}
