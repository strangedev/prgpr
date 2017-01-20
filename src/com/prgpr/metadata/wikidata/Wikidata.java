package com.prgpr.metadata.wikidata;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.prgpr.data.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by strange on 1/16/17.
 * @author Noah Hummel
 *
 * A class for interacting with the wikidata API (https://www.mediawiki.org/wiki/API:Main_page).
 * It supports looking up entity ids and article names, getting claims (statements) about entities
 * by their id and some preselection foo because the API is a mess.
 */
public class Wikidata {

    private static final Logger log = LogManager.getFormatterLogger(Wikidata.class);

    /**
     * Lookup method for getting the internal entity id mediawiki uses for it's wikidata project.
     * For some reason there's no mapping between page id's and entities, which registers as a solid
     * 8.5 on the API-horribleness scale, because entity ids provide the main entry point for looking up claims.
     * Well done.
     * This version just uses plain text search and hopes for the best. Works okay in most cases. (Totally not good
     * pratice though, shame on you mediawiki!)
     *
     * @param articleName The article whose entity id to look up
     * @param namespaceId The namespace the article is in to avoid multiple results
     * @return The internal id used by mediawiki (entityId) or "" is not found.
     */
    public static String getEntityId(String articleName, int namespaceId) {

        final String NOT_FOUND = "NOT_FOUND";

        log.info("Looking up wikidata entity id for " + articleName);
        String result = "";
        List<String> entityIds = new LinkedList<>();

        String baseRequest = "https://www.wikidata.org/w/api.php?format=json&action=wbgetentities&sites=dewiki&titles=";
        try {
            baseRequest += URLEncoder.encode(articleName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.catching(e);
            log.error("The article name contained symbols not encodable by UTF-8. This should be damned near impossible.");
            return NOT_FOUND;
        }

        try {
            result = HttpRequest.get(baseRequest);
        } catch (Exception e) {
            log.catching(e);
            log.error("The HTTP request died.");
            return NOT_FOUND;
        }

        JsonObject jsonRoot = JsonObject.readFrom(result);
        JsonObject entities = jsonRoot.get("entities").asObject();

        if (entities == null) {
            log.error("Received malformed json.");
            return NOT_FOUND;
        }
        if (entities.get("-1") != null) {
            log.warn("Entity for article " + articleName + " wasn't found.");
            return NOT_FOUND;
        }

        entities.forEach(entity -> {
            JsonObject properties = entity.getValue().asObject();
            if (properties == null) {
                log.warn("Parsed entity contains no data.");
                return;
            }

            int entityNamespaceId = properties.get("ns").asInt();
            if (namespaceId == entityNamespaceId) entityIds.add(properties.get("id").asString());
        });

        if (entityIds.isEmpty()) {
            log.warn("No matching entity with correct namespace for " +  articleName + " was found.");
            return NOT_FOUND;
        }
        return entityIds.get(0);

    }

    /**
     * Reverse lookup which maps entity ids to article names. See getEntityId for the basic gist of it.
     *
     * @param entityId The entity id of the entity whose name to look up
     * @return The name of the associated article or "" if not found
     */
    public static String getPageTitle(String entityId) {
        log.info("Looking up article name for wikidata entity with id " + entityId);
        String result = "";
        String baseRequest = "https://www.wikidata.org/w/api.php?format=json&action=wbsearchentities&language=de&search=";
        try {
            baseRequest += URLEncoder.encode(entityId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.catching(e);
            log.error("The entity id contained symbols not encodable by UTF-8. This should be damned near impossible.");
            return "";
        }
        try {
            result = HttpRequest.get(baseRequest);
        } catch (Exception e) {
            log.catching(e);
            log.error("The HTTP request died.");
            return "";
        }

        JsonObject jsonRoot = JsonObject.readFrom(result);
        JsonArray searchResults = jsonRoot.get("search").asArray();
        if (searchResults.isEmpty()) {
            log.warn("Entity with id " + entityId + " does not show in search results.");
            return "";
        }
        JsonObject firstMatch = searchResults.get(0).asObject();
        JsonValue label = firstMatch.get("label");
        if (label == null) {
            log.warn("Search results for entity " + entityId + " show no article name.");
            return "";
        }
        return label.asString();
    }

    /**
     * So for some reason mediawiki calls data attributes associated with claims "snaks". I don't have a clue what
     * that's supposed to mean. If you're pondering life's big questions, if you're in search of true asthetics,
     * try to read json from the API yourself, it will be a spiritual experience.
     *
     * @param entityId The god forsaken entity id
     * @param propertyId The property id of the claim to get. They all have cryptic names, because that's really #cyber and stuff.
     * @return Really, who wrote this spec? Returns the "snak", i.e. the Json containing the actual data, as well as metadata about the data, sprinkled with some data about the metadata including redundancies and backups in case you get confused while reading the json and don't want to start from the top.
     */
    public static JsonObject getSnak(String entityId, String propertyId) {
        log.info("Getting claims " + propertyId + " for entity with id " + entityId);
        String result = "";

        String baseRequest = "https://www.wikidata.org/w/api.php?format=json&action=wbgetclaims&entity=";
        try {
            baseRequest += URLEncoder.encode(entityId, "UTF-8");
            baseRequest += "&property=";
            baseRequest += URLEncoder.encode(propertyId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.catching(e);
            log.error("The entity id or properties contained symbols not encodable by UTF-8. This should be damned near impossible.");
            return null;
        }

        try {
            result = HttpRequest.get(baseRequest);
        } catch (Exception e) {
            log.catching(e);
            log.error("The HTTP request died.");
            return null;
        }

        JsonObject jsonRoot = JsonObject.readFrom(result);
        JsonObject claims;
        JsonValue claim;

        if (jsonRoot != null) {
            claims = jsonRoot.get("claims").asObject();
            claim = claims.get(propertyId);
            if (claim != null) {
                return selectLatestSnak(claim.asArray());
            }
        }
        log.warn("No suitable claims were found.");
        return null;
    }

    /**
     * Because this is a really good API, there are multiple data points for most claims.
     * And because just being able to decide which of those to use is too easy, the flag marking the preferred claim
     * to use when looking up a "snak" is almost never set.
     * They don't bother, I won't bother, this just takes the latest entry and prays.
     * @param mainsnaks A JsonArray of candidate "snak"s
     * @return The most bestest "snak" of them all
     */
    private static JsonObject selectLatestSnak(JsonArray mainsnaks) {
        return streamJsonArray(mainsnaks)
                .map(JsonValue::asObject)
                .reduce(mainsnaks.get(0).asObject(), (aSnak, anotherSnak) -> {
                    String aDateString = SnakExtractor.parsePointInTimeQualifier(aSnak);
                    String anotherDateString = SnakExtractor.parsePointInTimeQualifier(anotherSnak);

                    if (aDateString != null && anotherDateString == null) return aSnak;
                    if (aDateString == null && anotherDateString != null) return anotherSnak;
                    if (aDateString == null && anotherDateString == null) return anotherSnak;

                    com.prgpr.data.Date aDate = new Date(aDateString);
                    com.prgpr.data.Date anotherDate = new Date(anotherDateString);

                    if (aDate.greaterThan(anotherDate)) return aSnak;
                    if (anotherDate.greaterThan(aDate)) return anotherSnak;

                    return anotherSnak;
        });
    }

    /**
     * Bodge productions present: The StreamSupport-Spliterator-Iterator-Pattern
     * - because being able to stream iterators by default is not "compatible" enough.
     * @param array A gosh darn json array to stream
     * @return A bloody stream of it
     */
    private static Stream<JsonValue> streamJsonArray(JsonArray array) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(array.iterator(), Spliterator.ORDERED), false);
    }

}
