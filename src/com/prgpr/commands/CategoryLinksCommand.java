package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.neo4j.Neo4jElement;
import com.prgpr.framework.database.neo4j.Neo4jEmbeddedDatabase;
import com.prgpr.framework.database.neo4j.Neo4jEmbeddedDatabaseFactory;
import com.prgpr.helpers.Benchmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * Created by kito on 19.11.16.
 */
public class CategoryLinksCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(Page.class);

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument()
    };

    @Override
    public String getName() {
        return "categorylinks";
    }

    @Override
    public String getDescription() {
        return "Inserts the links of the categories.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
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
        File f = new File(arguments[0].get());
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(f);
        Neo4jEmbeddedDatabase graphDb = Neo4jEmbeddedDatabaseFactory.newEmbeddedDatabase(db);

        long time = Benchmark.run(()->{
            graphDb.transaction(() ->{
                for (Node node : db.getAllNodes()) {
                    Page page = new Page(new Neo4jElement(graphDb, node));
                    page.insertCategoryLinks();
                    page.getCategories().forEach(catPage -> {
                        System.out.println("Category from DB: " + catPage.getTitle());
                    });
                }
            });
        });

        log.info("It took so many seconds: " + time / 1000);
    }
}
