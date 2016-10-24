package com.prgpr;

import com.prgpr.collections.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

import com.prgpr.exceptions.MalformedWikidataException;
// import com.prgpr.LinkExtraction;
import com.prgpr.mock.LinkExtraction;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * A Factory class which creates a Set of Page objects from parsing a file of wikidata.
 */
public class PageFactory {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);
    private static final int CHUNK_SIZE = 1000; // TODO create config file, tweak this number

    /**
     * Extracts a Set of pages from a file of wikidata.
     *
     * @param infilePath The path to the file of wikidata.
     * @return A Set of contained Pages.
     * @throws MalformedWikidataException If the input is malformed.
     */
    public static Set<Page> extractPages(String infilePath) throws MalformedWikidataException{

        Set<Page> setToReturn = new LinkedHashSet<>();
        ThreadedArticleCollector articleCollector = new ThreadedArticleCollector(infilePath, CHUNK_SIZE);
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
