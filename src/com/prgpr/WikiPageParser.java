package com.prgpr;

import com.prgpr.collections.Tuple;
import com.prgpr.exceptions.MalformedWikidataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

public class WikiPageParser {

    private static final Logger log = LogManager.getFormatterLogger(WikiPageParser.class);
    private static final Character articleDelimiter = '¤';

    private boolean insideArticle = false;

    private ArrayList<String> aggregatedLines = new ArrayList<>();
    private Tuple<Page, String> protoPage;
    private Page tmpPage = null;

    public Tuple<Page, String> getProtoPage() throws IllegalAccessException {
        if (protoPage == null) {
            log.error("protoPage accessed before creation");
            throw new IllegalAccessException("protoPage accessed before creation");
        }
        return protoPage;
    }

    public boolean parseLine(String line) throws MalformedWikidataException {

        if(line.length() > 0) {

            if(line.charAt(0) == articleDelimiter){ // Delimiter encountered.

                if(insideArticle) { // There was already a delimiter in a previous line, the article is complete.

                    insideArticle = false; // Flip the flag!

                    if(tmpPage == null){ // If there was an error before, we can't complete this article.
                        log.error("Malformed article encountered");
                        throw new MalformedWikidataException();
                    }

                    Optional<String> maybeArticle = aggregatedLines.stream()
                            .reduce((x, y) -> x + y);

                    if (maybeArticle.isPresent()) {
                        protoPage = new Tuple<>(tmpPage, maybeArticle.get());

                    } else {
                        log.error("Empty article encountered");
                        throw new MalformedWikidataException("Empty article encountered");

                    }

                    aggregatedLines.clear();

                    // A complete article has been parsed.
                    return true;

                } else { // This is a new delimiter, negate delimiterEncountered and parse some info.

                    insideArticle = true; // Flip the flag!

                    String[] attributeStrings = line.split("\\s+"); // strip whitespace from attributes line

                    // Attribute string needs to be wll formed, were not trying to do error correction.
                    if (attributeStrings.length < 4) {
                        log.error("Page attributes are missing");
                        throw new MalformedWikidataException("Page attributes are missing");
                    }

                    long pageId;
                    try {
                        pageId = Long.parseLong(attributeStrings[1]);
                    } catch (NumberFormatException exception) {
                        log.error("Page attribute 'pageId' is malformed");
                        throw new MalformedWikidataException("Page attribute 'pageId' is malformed");
                    }

                    int namespaceId;
                    try {
                        namespaceId = Integer.parseInt(attributeStrings[2]);
                    } catch (NumberFormatException exception) {
                        log.error("Page attribute 'namespaceId' is malformed");
                        throw new MalformedWikidataException("Page attribute 'namespaceId' is malformed");
                    }

                    String title = "";
                    for (int i = 3; i < attributeStrings.length; i++) {
                        title += " " + attributeStrings[i]; // TODO: 10/23/16 retain original whitespace
                    }

                    tmpPage = new Page(pageId, namespaceId, title); // prepare page object, this will be given to other
                                                                    // parsers alongside with the article contents.
                                                                    // @todo Fix responsibilities
                }

            }

        }

        if(insideArticle){ // If we're inside of an article, aggregate all teh linez.
            aggregatedLines.add(line);

        }

        return false;

    }
}
