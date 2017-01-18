package com.prgpr;

import com.prgpr.data.City;
import com.prgpr.data.EntityBase;
import com.prgpr.data.Monument;
import com.prgpr.data.Person;
import com.prgpr.framework.AsciiTable;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;

import java.util.Set;

/**
 * Created by lissie on 1/15/17.
 * @author Elizaveta Kovalevskaya, Noah Hummel
 *
 * A class for evaluating a test set of 20 entities of each entity type.
 * Prints pretty tables, yay.
 */
public class Evaluation {

    private static final Logger log = LogManager.getFormatterLogger(Evaluation.class);

    private int[] person_attrib_correct_count = {
            0, 0, 0, 0, 0, 0, 0
    };

    private String[] person_attrib_names = {
            "First name",
            "Last name",
            "Birth name",
            "Date of birth",
            "Date of death",
            "Place of birth",
            "Place of death"
    };

    private String[][] persons_stuff = {
            {"Vanessa Lynn",
                    "Williams",
                    "",
                    "18. März 1963",
                    "",
                    "Tarrytown, New York",
                    ""},

            {"Uwe",
                    "Timm",
                    "",
                    "30. März 1940",
                    "",
                    "Hamburg",
                    "München/Berlin"},

            {"Karl",
                    "der Große",
                    "",
                    "2. April 747",
                    "28. Januar 814",
                    "",
                    ""},

            {"August",
                    "Hudler",
                    "",
                    "12. Dezember 1868",
                    "22. November 1905",
                    "Odelzhausen",
                    ""},

            {"Ulysses Simpson",
                    "Grant",
                    "Hiram Ulysses Grant",
                    "27. April 1822",
                    "23. Juli 1885",
                    "Point Pleasant, Ohio",
                    ""},

            {"Lucius Aurelius",
                    "Verus",
                    "",
                    "15. Dezember 130",
                    "169",
                    "",
                    "Rom"},

            {"Anita",
                    "Augspurg",
                    "",
                    "22. September 1857",
                    "20. Dezember 1943",
                    "Verden (Aller)",
                    "Schweizer Exil"},

            {"Ulrike",
                    "Sparr",
                    "",
                    "19. April 1957",
                    "",
                    "Hamburg",
                    ""},

            {"Käthe",
                    "Starke-Goldschmidt",
                    "Goldschmidt",
                    "27. September 1905",
                    "10. August 1990",
                    "Altona/Elbe",
                    "Hamburg-Orthmarschen"},

            {"Elvira",
                    "Bach",
                    "",
                    "22. Juni 1951",
                    "",
                    "Neuenhain (Taunus)",
                    "Berlin"},

            {"Otto",
                    "Lessing",
                    "",
                    "24. Februar 1846",
                    "22. November 1912",
                    "Düsseldorf",
                    "Schmargendorf"},

            {"Friedrich",
                    "Wöhlert",
                    "",
                    "16. September 1797",
                    "31. März 1877",
                    "Kiel",
                    ""},

            {"Isolde Maria Klara",
                    "Kurz",
                    "",
                    "21. Dezember 1853",
                    "6. April 1944",
                    "Stuttgart",
                    ""},

            {"Jacques",
                    "Chaban-Delmas",
                    "",
                    "7. März 1915",
                    "10. November 2000",
                    "Paris",
                    ""},

            {"Heinz",
                    "Sommer",
                    "",
                    "1894",
                    "1944",
                    "",
                    ""},

            {"Hormuzd",
                    "Rassam",
                    "",
                    "1826",
                    "16. September 1910",
                    "Mosul",
                    "Brighton"},

            {"Robert",
                    "Schumann",
                    "",
                    "8. Juni 1810",
                    "29. Juli 1856",
                    "Zwickau, Königreich Sachsen",
                    "Endenich, Bonn"},

            {"Arnold",
                    "Kramer",
                    "",
                    "17. Mai 1863",
                    "9. Mai 1918",
                    "Wolfenbüttel",
                    "Braunschweig"},

            {"Georg Friedrich Adolph",
                    "Schöner",
                    "",
                    "19. März 1774",
                    "1. Juli 1841",
                    "Mansbach bei Hersfeld",
                    "Bremer Altstadt"},

            {"Elfriede",
                    "Jelinek",
                    "",
                    "20. Oktober 1946",
                    "",
                    "Mürzzuschlag",
                    "Wien und München"}
    };

    private String[] persons = {"Vanessa Lynn Wiliams",
            "Uwe Timm",
            "Karl der Große",
            "August Hudler",
            "Ulysses S. Grant",
            "Lucius Verus",
            "Anita Augspurg",
            "Ulrike Sparr",
            "Käthe Starke-Goldschmidt",
            "Elvira Bach",
            "Otto Lessing",
            "Johann Friedrich Ludwig Wöhlert",
            "Isolde Kurz",
            "Jacques Chaban-Delmas",
            "Heinz Sommer (Widerstandskämpfer)",
            "Hormuzd Rassam",
            "Robert Schumann",
            "Arnold Kramer",
            "Georg Friedrich Adolph Schöner",
            "Elfriede Jelinek"};

    private int[] city_attrib_correct_count = {
            0, 0, 0, 0
    };

    private String[] city_attrib_names = {
            "Name",
            "Country",
            "Population",
            "Earliest mention"
    };

    private String[][] cities_stuff = {
            {"Aurich (ostfr. Platt: Auerk)",
                    "Deutschland",
                    "41.489",
                    "1276"},

            {"Frankfurt am Main",
                    "Deutschland",
                    "732.688",
                    "794"},

            {"Perštejn (deutsch Pürstein)",
                    "Tschechien",
                    "1.099"},

            {"Tallinn",
                    "Estland",
                    "429.899",
                    "1238"},

            {"Saint Petersburg",
                    "Vereinigte Staaten",
                    "249.688",
                    "1876"},

            {"Frankfurt (Oder)",
                    "Deutschland",
                    "58.092",
                    ""},

            {"Heidelberg",
                    "Deutschland",
                    "156.267",
                    "1196"},

            {"Mannheim",
                    "Deutschland",
                    "305.780",
                    "766"},

            {"Cuxhaven",
                    "Deutschland",
                    "48.264",
                    ""},

            {"Bützow",
                    "Deutschland",
                    "7848",
                    "150"},

            {"Waldkirchen",
                    "Deutschland",
                    "10.283",
                    ""},

            {"Jabluniw",
                    "Westukraine",
                    "2.032",
                    "1593"},

            {"Heilbronn",
                    "Deutschland",
                    "122.567",
                    "741"},

            {"London",
                    "England",
                    "8.538.689",
                    "50"},

            {"Irdning",
                    "Österreich",
                    "2749",
                    "1140"},

            {"Tours",
                    "Frankreich",
                    "134.803",
                    ""},

            {"Frankfort",
                    "Vereinigte Staaten",
                    "25.527",
                    "1786"},

            {"Weimar",
                    "Deutschland",
                    "64.131",
                    "3. Juni 975"},

            {"Arendsee (Altmark)",
                    "Deutschland",
                    "6929",
                    "822"},

            {"Loitsche-Heinrichsberg",
                    "Deutschland",
                    "955",
                    "2010"}
    };

    private String[] cities = {"Aurich",
            "Frankfurt am Main",
            "Perštejn (deutsch Pürstein)",
            "Tallinn",
            "Saint Petersburg",
            "Frankfurt (Oder)",
            "Heidelberg",
            "Mannheim",
            "Cuxhaven",
            "Bützow",
            "Waldkirchen",
            "Jabluniw",
            "Heilbronn",
            "London",
            "Irdning",
            "Tours",
            "Frankfort (Kentucky)",
            "Weimar",
            "Arendsee (Altmark)",
            "Loitsche-Heinrichsberg"};

    private int[] monument_attrib_correct_count = {
            0, 0, 0, 0
    };

    private String[] monument_attrib_names = {
            "Name",
            "Nearest city",
            "Inauguration date",
            "Commemorated person"
    };

    private String[][] monuments_stuff = {
            {"Deutsche Kriegsgräberstätte auf dem Georgenberg",
                    "Spremberg",
                    "",
                    ""},

            {"Erich Kästner Museum",
                    "Dresden",
                    "",
                    "Erich Kästner"},

            {"Begegnungsstätte Alte Synagoge",
                    "Wuppertal",
                    "April 1994",
                    ""},

            {"Schillerdenkmal",
                    "Hannover",
                    "",
                    "Schiller"},

            {"Grotte mit Wasserspielen",
                    "Gotha",
                    "",
                    ""},

            {"Madonna",
                    "Bad Kissingen",
                    "",
                    ""},

            {"Statue des Babak",
                    "Naxçıvan",
                    "",
                    ""},

            {"Fallen Astronaut",
                    "",
                    "",
                    ""},

            {"Sport",
                    "Berlin",
                    "25. Juni 1963",
                    ""},

            {"Caracallabogen",
                    "Tebessa",
                    "",
                    "Caracalla, Julia Domna, Septimius Severus"},

            {"Sitzendes Paar",
                    "Bremen",
                    "",
                    ""},

            {"Wohnhaus Papst Benedikt XVI.",
                    "Pentling",
                    "",
                    ""},

            {"Mahnmal für die Magdeburger Widerstandskämpfer",
                    "Magdeburg",
                    "",
                    ""},

            {"Ehrenmal am Fuchsberg",
                    "Düren",
                    "",
                    ""},

            {"Johannes-Nepomuk-Statue",
                    "Wien",
                    "16. Mai 1863",
                    "Johannes Nepomuk"},

            {"Kriegerdenkmal 1809",
                    "Retz",
                    "26. September 1909",
                    ""},

            {"Arco della Vittoria",
                    "Genua",
                    "Eenhana-Schrein",
                    ""},

            {"Brester Festung",
                    "",
                    "",
                    ""},

            {"Gustav-Selve-Denkmal",
                    "Altena",
                    "1911",
                    "Gustav Selve"},

            {"Eenhana-Schrein",
                    "Eenhana",
                    "2008",
                    ""}
    };

    private String[] monuments = {"Deutsche Kriegsgräberstätte auf dem Georgenberg",
            "Erich Kästner Museum",
            "Begegnungsstätte Alte Synagoge Wuppertal",
            "Schillerdenkmal",
            "Grotte mit Wasserspielen",
            "Madonna (Bad Kissingen)",
            "Statue des Babak",
            "Fallen Astronaut",
            "Sport (Eduard Bargheer)",
            "Caracallabogen (Tebessa)",
            "Sitzendes Paar",
            "Wohnhaus Papst Benedikt XVI.",
            "Mahnmal für die Magdeburger Widerstandskämpfer",
            "Ehrenmal am Fuchsberg (Düren)",
            "Johannes-Nepomuk-Statue (Messeplatz)",
            "Kriegerdenkmal 1809",
            "Arco della Vittoria",
            "Brester Festung",
            "Gustav-Selve-Denkmal",
            "Eenhana-Schrein"};

    private void evaluatePersons(EmbeddedDatabase db) {

        String realAttribute;
        String discoveredAttribute;
        int counter_eval;

        for (int i = 0; i < 20; i++) {
            counter_eval = 0;

            log.info("Checking results for " + persons[i]);
            Person person = EntityFinder.getPersonByPageTitle(persons[i]);

            if (person == null) {
                log.error("Not found!");
                continue;
            }

            realAttribute = persons_stuff[i][counter_eval];
            discoveredAttribute = person.getFirstName();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                person_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = persons_stuff[i][counter_eval];
            discoveredAttribute = person.getLastName();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                person_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = persons_stuff[i][counter_eval];
            discoveredAttribute = person.getBirthName();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                person_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = persons_stuff[i][counter_eval];
            discoveredAttribute = person.getDateOfBirth();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                person_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = persons_stuff[i][counter_eval];
            discoveredAttribute = person.getDateOfDeath();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                person_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = persons_stuff[i][counter_eval];
            discoveredAttribute = person.getPlaceOfBirth();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                person_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = persons_stuff[i][counter_eval];
            discoveredAttribute = person.getPlaceOfDeath();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                person_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
        }

        AsciiTable t = new AsciiTable();
        t.setColumns("Attribute", "Correct", "Ratio");
        for (int i = 0; i < 7; i++) {
            t.addRow(
                    person_attrib_names[i],
                    person_attrib_correct_count[i],
                    person_attrib_correct_count[i]/20);
        }
        t.print();
    }

    private void evaluateCities(EmbeddedDatabase db) {

        String realAttribute;
        String discoveredAttribute;
        int counter_eval;

        for (int i = 0; i < 20; i++) {
            counter_eval = 0;

            log.info("Checking results for " + cities[i]);
            City city = EntityFinder.getCityByPageTitle(cities[i]);

            if (city == null) {
                log.error("Not found!");
                continue;
            }

            realAttribute = cities_stuff[i][counter_eval];
            discoveredAttribute = city.getName();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                city_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = cities_stuff[i][counter_eval];
            discoveredAttribute = city.getCountry();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                city_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = cities_stuff[i][counter_eval];
            discoveredAttribute = city.getPopulation();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                city_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = cities_stuff[i][counter_eval];
            discoveredAttribute = city.getEarliestMention();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                city_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
        }

        AsciiTable t = new AsciiTable();
        t.setColumns("Attribute", "Correct", "Ratio");
        for (int i = 0; i < 4; i++) {
            t.addRow(
                    city_attrib_names[i],
                    city_attrib_correct_count[i],
                    city_attrib_correct_count[i]/20);
        }
        t.print();
    }

    private void evaluateMonuments(EmbeddedDatabase db) {

        String realAttribute;
        String discoveredAttribute;
        Person tmpPerson;
        City tmpCity;
        int counter_eval;

        for (int i = 0; i < 20; i++) {
            counter_eval = 0;
            tmpCity = null;
            tmpPerson = null;

            log.info("Checking results for " + monuments[i]);
            Monument monument = EntityFinder.getMonumentByPageTitle(monuments[i]);

            if (monument == null) {
                log.error("Not found!");
                continue;
            }

            realAttribute = monuments_stuff[i][counter_eval];
            discoveredAttribute = monument.getName();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                monument_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = monuments_stuff[i][counter_eval];
            tmpCity = monument.getNearestCity();
            if (tmpCity != null) {
                discoveredAttribute = tmpCity.getName();
                if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                    monument_attrib_correct_count[counter_eval]++;
                    log.info("Attribute was discovered correctly.");
                } else {
                    log.info("Attribute wasn't discovered correctly.");
                }
            }
            counter_eval++;

            realAttribute = monuments_stuff[i][counter_eval];
            discoveredAttribute = monument.getInaugurationDate();
            if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                monument_attrib_correct_count[counter_eval]++;
                log.info("Attribute was discovered correctly.");
            } else {
                log.info("Attribute wasn't discovered correctly.");
            }
            counter_eval++;

            realAttribute = monuments_stuff[i][counter_eval];
            tmpPerson = monument.getCommemoratedPerson();
            if (tmpCity != null) {
                discoveredAttribute = tmpPerson.getRawName();
                if (realAttribute.equalsIgnoreCase(discoveredAttribute)) {
                    monument_attrib_correct_count[counter_eval]++;
                    log.info("Attribute was discovered correctly.");
                } else {
                    log.info("Attribute wasn't discovered correctly.");
                }
            }
        }

        AsciiTable t = new AsciiTable();
        t.setColumns("Attribute", "Correct", "Ratio");
        for (int i = 0; i < 4; i++) {
            t.addRow(
                    monument_attrib_names[i],
                    monument_attrib_correct_count[i],
                    monument_attrib_correct_count[i]/20);
        }
        t.print();
    }

     public void run() {

         EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase("neo4j/data");

         this.evaluatePersons(graphDb);
         this.evaluateCities(graphDb);
         this.evaluateMonuments(graphDb);

     }
}