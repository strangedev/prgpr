package com.prgpr;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import com.prgpr.LinkExtraction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by lisa on 10/24/16.
 * @author Elizaveta Kovalevskaya
 *
 * A LinkExtraction class which provied static methods for extracting informations from wikiarticles.
 */
public class LinkExtraction {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);

    /**
     * gets the categories from an article
     *
     * @param article A String of html-formated text
     * @return a LinkedHashSet of categories extended from the article
     */
    public static Set<String> extractCategories(String article){
        Set<String> categories = new LinkedHashSet<>();
        try{
            Document articles = Jsoup.parse(article);

            Elements links = articles.select("div#catlinks li a");
            links.forEach(link -> categories.add(link.text()));
        }

        catch (Exception except){
            log.error("Couldn't find categories in this article.");
        }
        return categories;
    }
}
