package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.*;
import com.prgpr.framework.database.RelationshipTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * @author Noah Hummel
 * Created by strange on 11/25/16.
 */
public class TestDBCommand extends Command{

    private static final Logger log = LogManager.getFormatterLogger(TestDBCommand.class);

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument()
    };

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Tests internal database logic. For debug purposes only.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
    }

    @Override
    public void handleArguments(String[] args) throws InvalidNumberOfArguments, InvalidArgument {
        if (args.length != 1) {
            throw new InvalidNumberOfArguments();
        }

        arguments[0].set(args[0]);
    }

    @Override
    public void run() {
        File f = new File(arguments[0].get());
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(f);
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(db);


        Element vertex = graphDb.createElement("Elements", 0, (node) -> {
            node.setProperty(Page.PageAttribute.title, "Elem1");
        });
        Element vertox = graphDb.createElement("Elements", 1, (node) -> {
            node.setProperty(Page.PageAttribute.title, "Elem2");
        });
        Element vertix = graphDb.createElement("Elements", 2, (node) -> {
            node.setProperty(Page.PageAttribute.title, "Elem3");
        });

        vertex.createUniqueRelationshipTo(vertox, RelationshipTypes.categoryLink);
        vertex.createUniqueRelationshipTo(vertix, RelationshipTypes.categoryLink);

        SearchProvider.findImmediateOutgoing(vertex, RelationshipTypes.categoryLink)
                .forEach((neighbor) -> {
                    System.out.print(neighbor.getProperty(Page.PageAttribute.title) + " ");
                    System.out.println(neighbor.getLabels().count());
                });

    }

}
