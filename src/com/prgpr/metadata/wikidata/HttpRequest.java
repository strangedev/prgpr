package com.prgpr.metadata.wikidata;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by strange on 1/16/17.
 * @author Noah Hummel
 *
 * A class wrapping a basic http GET request for convenience.
 * Nothing to see here.
 */
public class HttpRequest {

    private static final Logger log = LogManager.getFormatterLogger(HttpRequest.class);

    /**
     * Runs an http GET request and returns the result a string. Pure convenience.
     * @param requestUrl The url for the request
     * @return The result body as string
     * @throws Exception If the connection dies for strange reasons.
     */
    public static String get(String requestUrl) throws Exception {
        log.debug("Executing HTTP GET on: " + requestUrl);
        StringBuilder result = new StringBuilder();

        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        return result.toString();
    }

}
