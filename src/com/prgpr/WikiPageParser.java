package com.prgpr;

import com.prgpr.collections.Tuple;
import com.prgpr.exceptions.MalformedWikidataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

/**
 * WikiPageParser TODO: add author
 *
 * A stateful class which generates ProtoPages TODO: implement ProtoPage class
 * by reading one line of input at a time.
 */
public class WikiPageParser {

    private static final Logger log = LogManager.getFormatterLogger(WikiPageParser.class);
    private static final Character articleDelimiter = '¤';

    private boolean insideArticle = false;

    private ArrayList<String> aggregatedLines = new ArrayList<>();
    private Tuple<Page, String> protoPage;
    private Page tmpPage = null;

    /**
     * Getter method for the generated ProtoPage. Should only be used, after parseLine() returned true to indicate that
     * an article has been completely parsed. Needs to be called before parseLine() is called again, because the
     * ProtoPage will be overwritten by it.
     *
     * @return The current state of the generated ProtoPage
     * @throws IllegalAccessException If the ProtoPage is accessed before it was parsed completely. TODO: test behaviour
     */
    public Tuple<Page, String> getProtoPage() throws IllegalAccessException {
        if (protoPage == null) {
            log.error("protoPage accessed before creation");
            throw new IllegalAccessException("protoPage accessed before creation");
        }
        return protoPage;
    }

    /**
     * Parses a line of input. Aggregates all of the parsed lines until an ending delimiter is encountered.
     * If an ending delimiter is encountered, a ProtoPage will be generated which can be accessed by getProtoPage().
     * This is indicated by parseLine() returning true.
     *
     * @param line A line of sequentially passed input.
     * @return An indicator whether a whole article has been parsed.
     * @throws MalformedWikidataException If the input is malformed.
     */
    public boolean parseLine(String line) throws MalformedWikidataException {

        if(line.length() > 0) { // ignore empty lines, there's nothing to aggregate - saves some time.

            if(line.charAt(0) == articleDelimiter){ // Delimiter encountered. We don't know if starting or ending yet.

                if(insideArticle) { // There was already a delimiter in a previous line, the article is complete.

                    insideArticle = false; // Flip the flag! We're closing an article.

                    if(tmpPage == null){ // If there was an error before, we can't complete this article.
                        log.error("Malformed article encountered");
                        throw new MalformedWikidataException();

                    }

                    Optional<String> maybeArticle = aggregatedLines.stream() // Create a string from aggregated lines.
                            .reduce((x, y) -> x + y);

                    if (maybeArticle.isPresent()) { // .reduce() returns optional, be careful, it might not exist!
                        protoPage = new Tuple<>(tmpPage, maybeArticle.get());

                    } else { // If .reduce() never did anything, there were no items in aggregatedLines.
                        log.error("Empty article encountered");
                        throw new MalformedWikidataException("Empty article encountered");

                    }

                    aggregatedLines.clear(); // make room for the next article

                    return true; // A complete article has been parsed.

                } else { // This is a new delimiter, set the flag and parse some info.

                    insideArticle = true; // We're now inside an article.
                    protoPage = null;
                    String[] attributeStrings = line.split("\\s+"); // Strip whitespace from attributes line

                    // Attribute string needs to be well formed, were not trying to do error correction.
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

                    String title = ""; // The title may be longer than just a word, aggregate all remaining strings.
                    for (int i = 3; i < attributeStrings.length; i++) {
                        title += " " + attributeStrings[i]; // TODO: 10/23/16 retain original whitespace

                    }

                    // prepare page object, this will be given to other parsers alongside with the article contents.
                    tmpPage = new Page(pageId, namespaceId, title); // TODO parsing should happen elsewhere
                }

            }

        }

        if(insideArticle) aggregatedLines.add(line);  // If we're inside of an article, aggregate all teh linez.

        return false; // We're inside of an article, the article can't be complete.

    }

}
