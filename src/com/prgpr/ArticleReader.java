package com.prgpr;

import com.prgpr.exceptions.MalformedWikidataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by kito on 10/26/16.
 */
public class ArticleReader extends Producer<ProtoPage> {

    private static final Logger log = LogManager.getFormatterLogger(ArticleReader.class);
    private boolean insideArticle = false;
    private ProtoPage current;
    private StringBuilder currentDocument = new StringBuilder();
    private String wikiFilePath;

    ArticleReader(String wikiFilePath){
        this.wikiFilePath = wikiFilePath;
    }

    public void run(){
        try (Stream<String> stream = Files.lines(Paths.get(wikiFilePath))) {
            stream.forEachOrdered(this::parseLine);
        }
        catch (IOException exception) {
            log.error("Couldn't get lines of file: " + wikiFilePath);
        }
        this.done();
    }

    private void parseLine(String line) {
        if(line.isEmpty()) return;  // Ignores empty lines

        switch(line.charAt(0)){  // Check for delimiter
            case '¤':
                if(this.insideArticle){
                    this.insideArticle = false;

                    if(line.length() > 1){  // If an opening delimiter is encountered before a closing one
                        this.current = null;
                        this.currentDocument = null;
                        return;
                    }
                    this.current.setHtmlData(this.currentDocument.toString());
                    this.emit(new Consumable<>(this.current));  // Notify subscribers

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
                        throw new MalformedWikidataException(e.getMessage());
                    }
                } else {  // first line had no metadata
                    throw new MalformedWikidataException();
                }
                this.current = new ProtoPage(  // Start parsing lines of article
                        new Page(id, namespaceId, m.group(3))
                );
                this.currentDocument = new StringBuilder();
                this.insideArticle = true;
                break;

            default:
                this.currentDocument.append(line);
                break;
        }
    }
}