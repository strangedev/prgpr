package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectory;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.Label;
import com.prgpr.framework.database.PropertyValuePair;
import com.prgpr.framework.database.SearchProvider;
import com.prgpr.framework.database.neo4j.Neo4jElement;
import com.prgpr.framework.database.neo4j.Neo4jEmbeddedDatabase;
import com.prgpr.framework.database.neo4j.Neo4jEmbeddedDatabaseFactory;
import com.prgpr.framework.database.neo4j.RelationshipTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.LinkedHashSet;

/**
 * Created by strange on 11/25/16.
 */
public class TestDBCommand extends Command{

    private static final Logger log = LogManager.getFormatterLogger(Page.class);

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectory()
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
        Neo4jEmbeddedDatabase graphDb = Neo4jEmbeddedDatabaseFactory.newEmbeddedDatabase(db);


        Element vertex = graphDb.createElement("Elements", 0, (node) -> {});
        Element vertox = graphDb.createElement("Elements", 1, (node) -> {});
        Element vertix = graphDb.createElement("Elements", 1, (node) -> {});

        graphDb.getAllElements().forEach((element) -> {
            System.out.print(element.getProperty(Page.PageAttribute.id) + " ");
            System.out.println(element.getLabels().count());
        });

        vertex.createUniqueRelationshipTo(vertox, RelationshipTypes.categoryLink);
        vertex.createUniqueRelationshipTo(vertox, RelationshipTypes.categoryLink);

        SearchProvider.findAnyImmediateIncoming(vertox, new Label() {
            @Override
            public String name() {
                return "";
            }
        }, RelationshipTypes.categoryLink, new LinkedHashSet<PropertyValuePair>())
                .forEach((neighbor) -> {
                    System.out.print(neighbor.getProperty(Page.PageAttribute.id) + " ");
                    System.out.println(neighbor.getLabels().count());
                });

    }

}
