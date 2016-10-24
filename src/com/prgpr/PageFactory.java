package com.prgpr;

import com.prgpr.collections.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

import com.prgpr.exceptions.MalformedWikidataException;
import com.prgpr.ThreadedArticleCollector;
// import com.prgpr.LinkExtraction;
import com.prgpr.mock.LinkExtraction;

/**
 * Created by strange on 10/21/16.
 *
 */
public class PageFactory {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);
    private static final int chunkSize = 1000; // @todo create config file, tweak this number

    public static Set<Page> extractPages(String infilePath) throws MalformedWikidataException{

        Set<Page> setToReturn = new LinkedHashSet<>();
        ThreadedArticleCollector articleCollector = new ThreadedArticleCollector(infilePath, chunkSize);
        articleCollector.start();

        while (articleCollector.hasNext()) {

            ArrayList<Tuple<Page, String>> nextChunk =  articleCollector.next();

            nextChunk.parallelStream()
                     .forEach(tuple -> tuple.x.setCategories(LinkExtraction.extractCategories(tuple.y)));

            nextChunk.forEach(tuple -> setToReturn.add(tuple.x)); // @todo Memory bottleneck here!

        }

        return setToReturn;
        
    }

}
