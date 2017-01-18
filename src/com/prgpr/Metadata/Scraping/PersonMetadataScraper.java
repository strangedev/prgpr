package com.prgpr.Metadata.Scraping;

import com.prgpr.data.Page;
import com.prgpr.data.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by strange on 1/16/17.
 * @author Noah Hummel
 *
 * A class for scraping embedded wikidata metadata tags from
 * htmls associated to Person entities.
 */
public class PersonMetadataScraper {

    private static final Logger log = LogManager.getFormatterLogger(PersonMetadataScraper.class);

    /**
     * Scrapes embedded wikidata metadata from an html associated to a Person entity.
     * Most Persons in wikipedia include embedded metadata in a hidden tag, this method scrapes it
     * into a dictionary using jsoup.
     *
     * @param person The person entity whose html to scrape
     * @return A dictionary containing all scraped info, fields that're not found are blank.
     */
    public static HashMap<Person.PersonAttribute, String> scrapeMetadataTag(Person person) {

        log.info("Scraping embedded metadata tag for " + person.getTitle());

        HashMap<Person.PersonAttribute, String> metadata = new LinkedHashMap<>();  // This will be retval

        Document document = Jsoup.parse(person.getSource().getHtml());
        Elements matches = document.select("table#Vorlage_Personendaten tbody tr");  // locate hidden tag
        System.out.println(matches.size());

        for (org.jsoup.nodes.Element match : matches) {

            if (match.children().size() < 2) continue;  // table contains at minimum description and data

            org.jsoup.nodes.Element descriptor = match.child(0);
            org.jsoup.nodes.Element value = match.child(1);

            log.debug("Encountered metadata with descriptor " + descriptor.text());
            switch (descriptor.text()) {  // giant switch case for different case of table entries
                case "NAME":
                    metadata.put(Person.PersonAttribute.RAW_NAME, value.text());

                    if (metadata.containsKey(Person.PersonAttribute.LAST_NAME)) break;  // don't overwrite full names
                    String[] names = value.text().split(",\\s+");
                    metadata.put(Person.PersonAttribute.LAST_NAME, names[0]);

                    if (names.length < 2) break;
                    metadata.put(Person.PersonAttribute.FIRST_NAME, names[1]);

                    break;
                case "ALTERNATIVNAMEN":
                    String[] entries = value.text().split(";\\s+");

                    for (String entry : entries) {
                        if (entry.toLowerCase().endsWith("(vollständiger name)")) {
                            log.debug("Encountered an alternate full name for " + person.getTitle() + ", " + entry);
                            entry = entry.replaceAll("\\s+\\(.+\\)", "");
                            String[] containedNames = entry.split(",\\s+");
                            metadata.put(Person.PersonAttribute.LAST_NAME, containedNames[0]);

                            if (containedNames.length < 2) break;
                            metadata.put(Person.PersonAttribute.FIRST_NAME, containedNames[1]);

                        } else if (entry.toLowerCase().endsWith("(geburtsname)")) {
                            log.debug("Encountered an alternate birth name for " + person.getTitle() + ", " + entry);
                            metadata.put(Person.PersonAttribute.BIRTH_NAME, entry.replaceAll("\\s+\\(.+\\)", ""));
                        }
                    }

                    break;
                case "GEBURTSDATUM":
                    metadata.put(Person.PersonAttribute.DATE_OF_BIRTH, value.text());
                    break;
                case "GEBURTSORT":
                    metadata.put(Person.PersonAttribute.PLACE_OF_BIRTH, value.text());
                    break;
                case "STERBEDATUM":
                    metadata.put(Person.PersonAttribute.DATE_OF_DEATH, value.text());
                    break;
                case "STERBEORT":
                    metadata.put(Person.PersonAttribute.PLACE_OF_DEATH, value.text());
                    break;
            }
        }
        return metadata;
    }

    /**
     * For generating statistics to measure whether embedded tags can be used for extracting the required data.
     * Turns out that more than 90% of articles include this, so we only use this source, suck it.
     * @param person The person whose metadata to count
     * @return The number of metadata entries found in the persons html
     */
    public static int getMetadataCount(Person person) {
        Document document = Jsoup.parse(person.getSource().getHtml());
        Elements matches = document.select("table#Vorlage_Personendaten tbody tr");
        return matches.size();
    }

}
