package com.prgpr;

import com.prgpr.collections.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.prgpr.exceptions.MalformedWikidataException;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * A Factory class which creates a Set of Page objects from parsing a file of wikidata.
 */
public class PageFactory {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);
    private static int chunkSize; // TODO create config file, tweak this number
    private static int maxChunks; // TODO create config file, tweak this number

    /**
     * Extracts a Set of pages from a file of wikidata.
     *
     * @param infilePath The path to the file of wikidata.
     * @return A Set of contained Pages.
     * @throws MalformedWikidataException If the input is malformed.
     */
    public static Set<Page> extractPages(String infilePath) throws MalformedWikidataException{

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config/config.properties");
            prop.load(input);

            // get the property value and print it out
            chunkSize = Integer.parseInt(prop.getProperty("chunkSize"));
            maxChunks = Integer.parseInt(prop.getProperty("maxChunks"));

        } catch (IOException exception) {
            log.error("Couldn't read properties file.");
            exception.printStackTrace();

        } catch (NumberFormatException exception) {
            log.error("Properties file is malformed.");
            exception.printStackTrace();

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("Couldn't close properties file.");
                    e.printStackTrace();

                }

            }

        }


        Set<Page> setToReturn = new LinkedHashSet<>();
        ThreadedArticleCollector articleCollector = new ThreadedArticleCollector(infilePath, chunkSize, maxChunks);
        articleCollector.start();

        while (articleCollector.hasNext()) { // We're reading input in chunks of articles to cap memory usage

            ArrayList<Tuple<Page, String>> nextChunk = articleCollector.next();

            nextChunk.parallelStream() // chunks are processed in parallel to increase throughput.
                     .forEach(tuple -> tuple.x.setCategories(LinkExtraction.extractCategories(tuple.y)));

            nextChunk.forEach(tuple -> setToReturn.add(tuple.x)); // TODO Memory bottleneck here!

        }

        return setToReturn;
        
    }

}
