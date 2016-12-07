package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.exceptions.MalformedWikidataException;
import com.prgpr.framework.consumer.Producer;
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
 * @author Kyle Rinfreschi
 *
 * A Class producing Pages by reading the input file and emitting Pages.
 */
public class PageProducer extends Producer<Page> {

    private static final Logger log = LogManager.getFormatterLogger(PageProducer.class);

    private boolean insideArticle = false;
    private Page current;
    private StringBuilder currentDocument = new StringBuilder();
    private final String wikiFilePath;
    private long id;
    private int namespaceId;
    private String title;

    /**
     * Constructor.
     *
     * @param wikiFilePath The path to the input file.
     */
    public PageProducer(String wikiFilePath){
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
            case '\u00a4':
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
                    Page page = PageFactory.getPage(id, namespaceId, title, this.currentDocument);
                    this.emit(page);
                    this.currentDocument = null;
                    return;
                }

                Pattern r = Pattern.compile("\\s+([0-9]+)\\s+([0-9]+)\\s+(.*)");  // Regex for metadata in first line
                Matcher m = r.matcher(line);

                if (m.find()) {
                    try {
                        id = Long.parseLong(m.group(1));
                        namespaceId = Integer.parseInt(m.group(2));
                        title = m.group(3);
                        System.out.println(title);
                    } catch (Exception e){  // first line had malformed metadata
                        throw new MalformedWikidataException("Could not convert metadata string to primitive types: " + e.getMessage());
                    }
                } else {  // first line had no metadata
                    throw new MalformedWikidataException("First line of article had no valid metadata.");
                }

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
