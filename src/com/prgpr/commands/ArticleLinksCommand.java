package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectory;
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
 * Created by lisa on 11/23/16.
 */
public class ArticleLinksCommand extends Command {
    private static final Logger log = LogManager.getFormatterLogger(Page.class);

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectory()
    };

    @Override
    public String getName() {
        return "articlelinks";
    }

    @Override
    public String getDescription() {
        return "Inserts the links of the articles.";
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

        graphDb.transaction();

        long time = Benchmark.run(()->{
            for (Node node : db.getAllNodes()) {
                Page page = new Page(new Neo4jElement(graphDb, node));
                page.insertArticleLinks();
            }
        });

        log.info("It took so many seconds: " + time / 1000);
    }
}

