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

    public static void setDatabase(EmbeddedDatabase graphDb){
        PageFactory.db = graphDb;
    }

    public static Page getPage(long id, int namespaceID, String title, StringBuilder html)
    {
        return new Page(db, id, namespaceID, title, html.toString());
    }

    public static Page getPage(long id, int namespaceID, String title, String html)
    {
        return new Page(db, id, namespaceID, title, html);
    }

    public static Page getPage(Element e)
    {
        return new Page(e);
    }


    public static void commit()
    {
        db.commit();
    }
}
