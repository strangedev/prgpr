package com.prgpr.framework.restapi;

import com.prgpr.export.DataExport;
import com.prgpr.EntityFinder;
import com.prgpr.PageFinder;
import com.prgpr.data.City;
import com.prgpr.data.Monument;
import com.prgpr.data.Page;
import com.prgpr.data.Person;
import com.prgpr.export.SimpleXmlDocument;

import java.util.Set;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Created by strange on 1/20/17.
 * @author Noah Hummel
 */
public class Router {

    private static final int PORT = 8080;  // TODO

    /*
        ___ _  _ ____    ____ _  _ ____ ___  _ _  _ ____    ____ ____ _  _ ___ ____ ____
         |  |__| |___    |__| |\/| |__|   /  | |\ | | __    |__/ |  | |  |  |  |___ |__/
         |  |  | |___    |  | |  | |  |  /__ | | \| |__]    |  \ |__| |__|  |  |___ |  \

     */

    public static void run() {

        port(PORT);

        /*
        ____ ____ _  _ ____ ____ _ ____    ____ ____ ____
        | __ |___ |\ | |___ |__/ | |       |___ |  | |  |
        |__] |___ | \| |___ |  \ | |___    |    |__| |__|

         */
        get("/help", (request, response) -> {
            return "Figure it out yourself.";  // TODO
        });


        /*
        ____ _ _  _ ___  _ _  _ ____    ___  ____ ____ ____ ____
        |___ | |\ | |  \ | |\ | | __    |__] |__| | __ |___ [__
        |    | | \| |__/ | | \| |__]    |    |  | |__] |___ ___]

         */

        get("/page/title/:arg",  (request, response) -> {
            Set<Page> matches = PageFinder.findAllByTitle(request.params(":arg"));
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendPages(matches, document);
            return DataExport.stringify(document);
        });


        /*
        ____ _ _  _ ___  _ _  _ ____    ___  ____ ____ ____ ____ _  _ ____
        |___ | |\ | |  \ | |\ | | __    |__] |___ |__/ [__  |  | |\ | [__
        |    | | \| |__/ | | \| |__]    |    |___ |  \ ___] |__| | \| ___]

         */

        get("/person/name/last/:arg",  (request, response) -> {
            Person match = EntityFinder.getAllPersons()
                            .filter(p -> p.getLastName().equals(request.params(":arg")))
                            .findFirst()
                            .orElse(null);
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendEntity(match, document);
            return DataExport.stringify(document);
        });

        get("/person/name/first/:arg",  (request, response) -> {
            Person match = EntityFinder.getAllPersons()
                    .filter(p -> p.getFirstName().equals(request.params(":arg")))
                    .findFirst()
                    .orElse(null);
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendEntity(match, document);
            return DataExport.stringify(document);
        });

        get("/person/name/birth/:arg",  (request, response) -> {
            Person match = EntityFinder.getAllPersons()
                    .filter(p -> p.getBirthName().equals(request.params(":arg")))
                    .findFirst()
                    .orElse(null);
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendEntity(match, document);
            return DataExport.stringify(document);
        });

        get("/person/name/raw/:arg",  (request, response) -> {
            Person match = EntityFinder.getAllPersons()
                    .filter(p -> p.getRawName().equals(request.params(":arg")))
                    .findFirst()
                    .orElse(null);
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendEntity(match, document);
            return DataExport.stringify(document);
        });


        /*
        ____ _ _  _ ___  _ _  _ ____    ____ _ ___ _ ____ ____
        |___ | |\ | |  \ | |\ | | __    |    |  |  | |___ [__
        |    | | \| |__/ | | \| |__]    |___ |  |  | |___ ___]

         */

        get("/city/name/:arg",  (request, response) -> {
            City match = EntityFinder.getAllCities()
                    .filter(c -> c.getName().equals(request.params(":arg")))
                    .findFirst()
                    .orElse(null);
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendEntity(match, document);
            return DataExport.stringify(document);
        });


        /*
        ____ _ _  _ ___  _ _  _ ____    _  _ ____ _  _ _  _ _  _ ____ _  _ ___ ____
        |___ | |\ | |  \ | |\ | | __    |\/| |  | |\ | |  | |\/| |___ |\ |  |  [__
        |    | | \| |__/ | | \| |__]    |  | |__| | \| |__| |  | |___ | \|  |  ___]

         */

        get("/city/name/:arg",  (request, response) -> {
            Monument match = EntityFinder.getAllMonuments()
                    .filter(c -> c.getName().equals(request.params(":arg")))
                    .findFirst()
                    .orElse(null);
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendEntity(match, document);
            return DataExport.stringify(document);
        });


        /*
        ___  ____ ____ _  _ ____ _  _    ___  _   _    ___  ____ ____ _ ____ _  _
        |__] |__/ |  | |_/  |___ |\ |    |__]  \_/     |  \ |___ [__  | | __ |\ |
        |__] |  \ |__| | \_ |___ | \|    |__]   |      |__/ |___ ___] | |__] | \|

         */

        get("/object/:arg",  (request, response) -> {
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendPage(PageFinder.findByDatabaseId(request.params(":arg")), document);
            DataExport.appendEntity(EntityFinder.findByDatabaseId(request.params(":arg")), document);
            return DataExport.stringify(document);
        });

        get("/all",  (request, response) -> {
            SimpleXmlDocument document = DataExport.newDocument();
            DataExport.appendPages(PageFinder.getAll(), document);
            DataExport.appendEntityStream(EntityFinder.getAll(), document);
            return DataExport.stringify(document);
        });

    }
}
