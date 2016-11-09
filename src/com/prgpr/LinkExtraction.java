package com.prgpr;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.LinkedHashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by lisa on 10/24/16.
 * @author Elizaveta Kovalevskaya
 *
 * A LinkExtraction class which provides static methods for extracting information from wiki articles.
 */
public class LinkExtraction {

    private static final Logger log = LogManager.getFormatterLogger(LinkExtraction.class);

    /**
     * Gets the categories from an article.
     *
     * @param article A String of html-formatted text.
     * @return A LinkedHashSet of categories extracted from the article.
     */
     public static Set<String> extractCategories(String article){

        Set<String> categories = new LinkedHashSet<>();

        try {
            Document articles = Jsoup.parse(article);
            Elements links = articles.select("div#catlinks li a");

            links.forEach(link -> categories.add(link.text()));

        } catch (Exception except){
            log.error("Couldn't find categories in this article.");
        }

        return categories;

    }
}
