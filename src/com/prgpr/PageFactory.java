package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.framework.consumer.Producer;
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
    private static GraphDatabaseService graphDb;

    public static void setDatabase(GraphDatabaseService graphDb){
        PageFactory.graphDb = graphDb;
    }

    public static Page getPage(long id, int namespaceID, String title)
    {
        return new Page(graphDb, id, namespaceID, title);
    }

    /**
     * Emits a previously created page.
     * This method was previously in a different class, but because
     * milestone goals had to be met, it resides here (for now).
     * Extracts the categories from the ProtoPages htmlData
     * before emitting.
     */
    /*
    private void emitPage(){
        this.current.getPage()
                .setCategories(
                        LinkExtraction.extractCategories(
                                this.current.getHtmlData().toString()  // Uses StringBuilder class
                        )
                );

        this.emit(this.current.getPage());  // only emit the resulting Page object
    }
    */
}
