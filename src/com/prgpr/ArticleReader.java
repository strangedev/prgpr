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
public class ArticleReader extends Producer {

    private static final Logger log = LogManager.getFormatterLogger(ArticleReader.class);
    private boolean insideArticle = false;
    private Page current;
    private StringBuilder current_document = new StringBuilder();
    private String wikiFilePath;

    ArticleReader(String wikiFilePath){
        this.wikiFilePath = wikiFilePath;
    }

    public void run(){
        try (Stream<String> stream = Files.lines(Paths.get(wikiFilePath))) {
            stream.forEachOrdered(this::delegateParseLine);
        }
        catch (IOException exception) {
            log.error("Couldn't get lines of file: " + wikiFilePath);
        }
    }

    private void delegateParseLine(String line) {
        if(line.isEmpty()) return;

        switch(line.charAt(0)){
            case '¤':
                if(this.insideArticle){
                    this.insideArticle = false;

                    if(line.length() > 1){
                        this.current = null;
                        this.current_document = null;
                        return;
                    }
                    // emit
                    return;
                }

                Pattern r = Pattern.compile("\\s+([0-9]+)\\s+([0-9])\\s+(.*)");

                // Now create matcher object.
                Matcher m = r.matcher(line);

                long id;
                int namespaceId;

                if (m.find()) {
                    try {
                        id = Long.parseLong(m.group(1));
                        namespaceId = Integer.parseInt(m.group(2));
                    } catch (Exception e){
                        throw new MalformedWikidataException(e.getMessage());
                    }
                } else {
                    throw new MalformedWikidataException();
                }

                this.current = new Page(id, namespaceId, m.group(3));
                this.current_document = new StringBuilder();
                this.insideArticle = true;
                break;
            default:
                this.current_document.append(line);
                break;
        }
    }
}
