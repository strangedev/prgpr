package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.data.ProtoPage;
import com.prgpr.exceptions.MalformedWikidataException;
import com.prgpr.framework.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Noah Hummel
 *
 * A Page Factory getting the text of the Wikidata used to generate Page objects.
 */
public class PageFactory extends Producer<Page> {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);

    private boolean insideArticle = false;
    private ProtoPage current;
    private StringBuilder currentDocument = new StringBuilder();
    private final String wikiFilePath;

    /**
     * Constructor.
     *
     * @param wikiFilePath The path to the input file.
     */
    PageFactory(String wikiFilePath){
        this.wikiFilePath = wikiFilePath;
    }

    /**
     * Streams lines of the input file to the parser "parseLine".
     */
    public void run(){
        try (Stream<String> stream = Files.lines(Paths.get(wikiFilePath), StandardCharsets.UTF_8)) {
            stream.forEachOrdered(this::parseLine);

        } catch (IOException exception) {
            log.error("Couldn't get lines of file: " + wikiFilePath);

        } catch (MalformedWikidataException exception) {
            log.error(exception.getMessage());

        }
        this.done();
    }

    /**
     * Emits a previously created page.
     * This method was previously in a different class, but because
     * milestone goals had to be met, it resides here (for now).
     * Extracts the categories from the ProtoPages htmlData
     * before emitting.
     */
    private void emitPage(){
        this.current.getPage()
                .setCategories(
                        LinkExtraction.extractCategories(
                                this.current.getHtmlData().toString()  // Uses StringBuilder class
                        )
                );

        this.emit(this.current.getPage());  // only emit the resulting Page object
    }

    /**
     * Parses an article by sequentially reading lines from a file.
     * Builds a persistent context between calls and emits a ProtoPage
     * when an article is finished.
     * If an ending delimiter is omitted, parseLine will treat the
     * current article as closed by default.
     *
     * @param line a String with the text of a line of the input file
     */
    private void parseLine(String line) {
        if(line.isEmpty()) return;  // Ignores empty lines

        switch(line.charAt(0)){  // Check for delimiter
            case '¤':
                boolean isSingleChar = line.length() == 1;

                // Closing delimiter received when not inside document
                if(!this.insideArticle && isSingleChar){
                    log.warn("Closing delimiter encountered while not inside document. Skipping article.");
                    return;
                }

                if(this.insideArticle){
                    // If an opening delimiter is encountered before a closing one
                    if(!isSingleChar){
                        log.warn("An opening delimiter was encountered before a closing one. Assuming article end.");
                    }

                    this.insideArticle = false;
                    this.current.setHtmlData(this.currentDocument);
                    this.emitPage();
                    this.current = null;  // reset internal fields
                    this.currentDocument = null;
                    return;
                }

                Pattern r = Pattern.compile("\\s+([0-9]+)\\s+([0-9]+)\\s+(.*)");  // Regex for metadata in first line
                Matcher m = r.matcher(line);

                long id;
                int namespaceId;

                if (m.find()) {
                    try {
                        id = Long.parseLong(m.group(1));
                        namespaceId = Integer.parseInt(m.group(2));
                    } catch (Exception e){  // first line had malformed metadata
                        throw new MalformedWikidataException("Could not convert metadata string to primitive types: " + e.getMessage());
                    }
                } else {  // first line had no metadata
                    throw new MalformedWikidataException("First line of article had no valid metadata.");
                }
                this.current = new ProtoPage(  // Start parsing lines of article
                        new Page(id, namespaceId, m.group(3))
                );
                this.currentDocument = new StringBuilder();
                this.insideArticle = true;
                break;

            default:
                if(this.currentDocument == null)  // if there was an error before and there's no article, don't append.
                    return;
                this.currentDocument.append(line);
                break;
        }
    }
}
