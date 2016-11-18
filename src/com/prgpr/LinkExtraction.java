package com.prgpr;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static Set<String> extractCategories(String article) {

        Set<String> categories = new LinkedHashSet<>();

        try {
            Document articles = Jsoup.parse(article);
            Elements links = articles.select("div#catlinks li a");

            links.forEach(link -> categories.add(link.text()));

        } catch (Exception except) {
            log.error("Couldn't find categories in this article.");
        }

        return categories;

    }

    /**
     * Gets the article links from an article.
     * Jsoup parses trough the text part and extracts all links.
     * By the regex all the files and external links will be deleted, so that only the internal links to the articles stay.
     *
     * @param article A String of html-formatted text.
     * @return A LinkedHashSet of wiki internal articles extracted from the article text.
     */

    public static Set<String> extractArticles(String article) {

        Set<String> articleLinks = new LinkedHashSet<>();

        try {
            Document articles = Jsoup.parse(article);
            Elements links = articles.select("div#mw-content-text p a");
            Pattern r = Pattern.compile("/wiki/([^:]*\")"); // only the /wiki/articlename are the internal articles

            for (Element link : links) {
                String test = link.toString();
                Matcher m = r.matcher(test); // parsing the link String to see if he is valid
                if (m.find()) { articleLinks.add(link.text()); }
            }
        } catch (Exception except) {
            log.error("Couldn't find articles in this article.");
        }
        return articleLinks;
    }
}