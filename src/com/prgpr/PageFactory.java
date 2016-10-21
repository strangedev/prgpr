package com.prgpr;

import java.io.IOException;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedList;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.prgpr.collections.Tuple;
import com.prgpr.exceptions.MalformedWikidataException;
// import com.prgpr.LinkExtraction;
import com.prgpr.mock.LinkExtraction;

/**
 * Created by strange on 10/21/16.
 *
 */
public class PageFactory {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);
    private static final Character articleDelimiter = '¤';

    private ConcurrentHashMap<Integer, Set<String>> categoriesForArticles = new ConcurrentHashMap<>();
    private LinkedList<Tuple<Page, String>> aggregatedProtoPages = new LinkedList<>();
    private LinkedList<String> aggregatedLines = new LinkedList<>();
    private boolean delimiterEncountered = false;
    private String tmpArticle;
    private long tmpPageId;
    private int tmpNamespaceId;
    private String tmpTitle;
    private Page tmpPage = null;

    private Page createProtoPageInstance(int namespaceId, String title) {
        return new Page(namespaceId, title);
    }

    private void encounterLine(String line) throws MalformedWikidataException{

        if(line.length() > 0) {

            if(line.charAt(0) == articleDelimiter){

                if(delimiterEncountered) {

                    if(tmpPage == null){
                        log.error("Malformed article encountered");
                        throw new MalformedWikidataException();
                    }

                    aggregatedLines.stream()
                                    .reduce((x, y) -> x + y)
                                    .ifPresent(x -> aggregatedProtoPages.add(new Tuple<>(tmpPage, x)));

                } else {

                    String[] attributeStrings = line.split("\\s+");

                    if (attributeStrings.length < 1) {
                        log.error("Page attributes are missing");
                        throw new MalformedWikidataException("Page attributes are missing");
                    }

                    try {
                        tmpPageId = Long.parseLong(attributeStrings[1]);
                    } catch (NumberFormatException exception) {
                        log.error("Page attribute 'pageId' is malformed");
                        throw new MalformedWikidataException("Page attribute 'pageId' is malformed");
                    }

                    try {
                        tmpNamespaceId = Integer.parseInt(attributeStrings[2]);
                    } catch (NumberFormatException exception) {
                        log.error("Page attribute 'namespaceId' is malformed");
                        throw new MalformedWikidataException("Page attribute 'namespaceId' is malformed");
                    }

                    tmpTitle = attributeStrings[3];
                    tmpPage = this.createProtoPageInstance(tmpNamespaceId, tmpTitle, tmpPageId);

                }

                delimiterEncountered = !delimiterEncountered;

            }

        }

        if(delimiterEncountered){
            aggregatedLines.add(line);

        }

    }
    
    public Set<Page> extractPages(String infilePath) throws MalformedWikidataException{

        Set<Page> setToReturn = new LinkedHashSet<>();

        try (Stream<String> stream = Files.lines(Paths.get(infilePath))) {
            stream.forEachOrdered(this::encounterLine);

        }
        catch (IOException exception) {
            log.error("Couldn't get lines of file: " + infilePath);

        }


        if(aggregatedProtoPages.size() > 0) {

            aggregatedProtoPages.parallelStream()
                    .forEach(tuple -> tuple.x.setCategories(
                            LinkExtraction.extractCategories(tuple.y)
                    ));

            aggregatedProtoPages.parallelStream()
                                .forEach(tuple -> setToReturn.add(tuple.x));

        }

        return setToReturn;

    }

}
