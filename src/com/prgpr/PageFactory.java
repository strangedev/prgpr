package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.framework.consumer.Producer;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.neo4j.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;

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

    public static void commit()
    {
        db.commit();
    }
}
