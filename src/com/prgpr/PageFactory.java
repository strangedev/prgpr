package com.prgpr;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.prgpr.collections.Tuple;
import com.prgpr.exceptions.MalformedWikidataException;
// import com.prgpr.LinkExtraction;
import com.prgpr.mock.LinkExtraction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by strange on 10/21/16.
 *
 */
public class PageFactory {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);
    
    public Set<Page> extractPages(String infilePath) throws MalformedWikidataException{

        Set<Page> setToReturn = new LinkedHashSet<>();

        WikiPageParser pageParser = new WikiPageParser();

        try (Stream<String> stream = Files.lines(Paths.get(infilePath))) {
            stream.forEachOrdered(pageParser::parseLine);

        }
        catch (IOException exception) {
            log.error("Couldn't get lines of file: " + infilePath);

        }

        if(pageParser.aggregatedProtoPages.size() > 0) {

            pageParser.aggregatedProtoPages.parallelStream()
                    .forEach(tuple -> tuple.x.setCategories(
                            LinkExtraction.extractCategories(tuple.y)
                    ));

            pageParser.aggregatedProtoPages.parallelStream()
                                .forEach(tuple -> setToReturn.add(tuple.x));

        }

        return setToReturn;

    }

}
