package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectory;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.DatabaseFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Created by kito on 19.11.16.
 */
public class CategoryLinksCommand extends Command {

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectory()
    };

    @Override
    public String getName() {
        return "categorylinks";
    }

    @Override
    public String getDescription() {
        return "This is an example description.";
    }

    @Override
    public void handleArguments(String[] args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.length < 1){
            throw new InvalidNumberOfArguments();
        }

        arguments[0].set(args[0]);
    }

    @Override
    public void run() {
        GraphDatabaseService graphDb = DatabaseFactory.newEmbeddedDatabase(arguments[0].get());

        // inserting the categories
        try ( Transaction tx = graphDb.beginTx() ) {
            for (Node node : graphDb.getAllNodes()) {
                Page page = new Page(node);
                page.insertCategorieLink();
            }
        }
    }
}
