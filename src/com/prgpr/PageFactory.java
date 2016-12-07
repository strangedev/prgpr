package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.framework.consumer.Producer;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Noah Hummel
 *
 * A Page Factory getting the text of the Wikidata used to generate Page objects.
 */
public class PageFactory extends Producer<Page> {

    private static final Logger log = LogManager.getFormatterLogger(PageFactory.class);
    private static EmbeddedDatabase db;

    /**
     * A function to set the database as the used by this instance.
     *
     * @param graphDb An embedded database which is used at runtime
     */
    public static void setDatabase(EmbeddedDatabase graphDb){
        PageFactory.db = graphDb;
    }

    /**
     * An function to create a Page with the given/extracted attributes.
     *
     * @param id Id of the Wikipedia page
     * @param namespaceID Defines which type Wikipedia page has
     * @param title Title of the Wikipedia page
     * @param html Html content of the Wikipedia page as a StringBuilder
     * @return The Page-Object with the String version of html.
     */
    public static Page getPage(long id, int namespaceID, String title, StringBuilder html)
    {
        return new Page(db, id, namespaceID, title, html.toString());
    }

    /**
     * An function to create a Page with the given/extracted attributes.
     *
     * @param id Id of the Wikipedia page
     * @param namespaceID Defines which type Wikipedia page has
     * @param title Title of the Wikipedia page
     * @param html Html content of the Wikipedia page as a String
     * @return The Page-Object.
     */
    public static Page getPage(long id, int namespaceID, String title, String html)
    {
        return new Page(db, id, namespaceID, title, html);
    }

    /**
     * A function to create a Page from an Element (from the database)
     *
     * @param e Element, which is used to create the Page-Object
     * @return The Page-Object.
     */
    public static Page getPage(Element e)
    {
        return new Page(e);
    }


    /**
     * Commits the changes to the database.
     */
    public static void commit()
    {
        db.commit();
    }
}
