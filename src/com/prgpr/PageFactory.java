package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.data.ProtoPage;
import com.prgpr.exceptions.MalformedWikidataException;
import com.prgpr.framework.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
        addDbConstraint(graphDb, "namespaceId");
        addDbConstraint(graphDb, "title");
    }

    public static Page getPage(long id, int namespaceID, String title)
    {
        return new Page(graphDb, id, namespaceID, title);
    }

    private static void addDbConstraint(GraphDatabaseService graphDb, String constaint){
        try ( Transaction tx = graphDb.beginTx() )
        {
            graphDb.schema()
                    .constraintFor( Label.label( "Page" ) )
                    .assertPropertyIsUnique( constaint )
                    .create();
            tx.success();
        }
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
