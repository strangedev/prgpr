package com.prgpr.commands;

import com.prgpr.data.Page;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.database.DatabaseFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Created by kito on 19.11.16.
 */
public class CategoryLinksCommand implements Command {
    @Override
    public String getName() {
        return "categorylinks";
    }

    @Override
    public int execute(String[] args) {
        GraphDatabaseService graphDb = DatabaseFactory.newEmbeddedDatabase(args[0]);

        // inserting the categories
        try ( Transaction tx = graphDb.beginTx() ) {
            for (Node node : graphDb.getAllNodes()) {
                Page page = new Page(node);
                page.insertCategorieLink();
            }
        }
        return 0;
    }
}
