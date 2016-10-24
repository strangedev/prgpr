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
 */
public class LinkExtraction {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);

    public static Set<String> extractCategories(String article){
        Set<String> categories = new LinkedHashSet<>();
        try{
            Document articles = Jsoup.parse(article);

            Elements links = articles.select("div#catlinks li a");
            for (Element link :links){
                //System.out.println(link.text());
                categories.add(link.text());
            }
        }

        catch (Exception except){
            log.error("Couldn't find categories in this article.");
        }
        return categories;
    }
}