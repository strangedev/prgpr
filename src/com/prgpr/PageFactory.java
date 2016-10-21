package com.prgpr;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedList;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.prgpr.Tuple;
import com.prgpr.exceptions.MalformedWikidataException;
// import com.prgpr.Page;
import com.prgpr.mock.Page;
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
    private LinkedList<Tuple<Page, String>> aggregatedArticles = new LinkedList<>();
    private LinkedList<String> aggregatedLines = new LinkedList<>();
    private boolean delimiterEncountered = false;
    private String tmpArticle;
    private int tmpNamespaceId;
    private String tmpTitle;
    private Page tmpPage = null;

    private createProtoPageInstance(int namespaceId, String title) {
        return new Page(namespaceId, title);
    }

    private void encounterLine(String line){

        if(line.length() > 0) {

            if(line.charAt(0) == articleDelimiter){

                if(delimiterEncountered) {

                    if(tmpPage == null){
                        log.error("Malformed article encountered");
                        throw new MalformedWikidataException();
                    }

                    aggregatedLines.stream()
                                    .reduce((x, y) -> x + y)
                                    .ifPresent(x -> aggregatedArticles.add(new Tuple<>(tmpPage, x)));



                } else {

                    tmpPage = this.createProtoPageInstance(tmpNamespaceId, tmpTitle);

                }

                delimiterEncountered = !delimiterEncountered;
            }

        }

        if(delimiterEncountered){

            aggregatedLines.add(line);

        }

    }
    
    public Set<Page> extractPages(String infilePath){

        Set<Page> setToReturn = new LinkedHashSet<>();

        try (Stream<String> stream = Files.lines(Paths.get(infilePath))) {
            stream.forEachOrdered(this::encounterLine);

        } catch (IOException exception) {
            log.error("Couldn't get lines of file: " + infilePath);

        }


        if(aggregatedArticles.size() > 0) {

            aggregatedArticles.parallelStream()
                                .map(x -> x.x.setCategories(LinkExtraction.extractCategories(x.y)))
                                .forEach(setToReturn::add);

        }

        return setToReturn;
    }

}
