package com.prgpr;

import com.prgpr.collections.Tuple;
import com.prgpr.exceptions.MalformedWikidataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

public class WikiPageParser {

    private static final Logger log = LogManager.getFormatterLogger(WikiPageParser.class);
    private static final Character articleDelimiter = '¤';

    public LinkedList<Tuple<Page, String>> aggregatedProtoPages = new LinkedList<>();

    private LinkedList<String> aggregatedLines = new LinkedList<>();
    private boolean delimiterEncountered = false;
    private long tmpPageId;
    private int tmpNamespaceId;
    private String tmpTitle;
    private Page tmpPage = null;

    private Page createProtoPageInstance(long id, int namespaceId, String title) {
        return new Page(id, namespaceId, title);
    }

    public void parseLine(String line) throws MalformedWikidataException {

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
                    tmpPage = this.createProtoPageInstance(tmpPageId, tmpNamespaceId, tmpTitle);

                }

                delimiterEncountered = !delimiterEncountered;

            }

        }

        if(delimiterEncountered){
            aggregatedLines.add(line);

        }

    }
}
