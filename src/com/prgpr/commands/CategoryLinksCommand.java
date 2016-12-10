package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.*;
import com.prgpr.helpers.Benchmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * @author Kyle Rinfreschi
 * Created by kito on 19.11.16.
 *
 * A Command which generates Database relationships between articles and
 * their corresponding categories.
 * From OLAT:
 *
 * "Durch den Aufruf einer entsprechenden Methode soll es möglich sein,
 *  dass systematisch alle Kategorien-Links der Wikipedia-Seiten in der Datenbank
 *  extrahiert und als typisierte Relationships (Achtung: nicht als Properties)
 *  in der Neo4J Graphdatenbank repräsentiert werden."
 *
 * "Kategorien-Links extrahieren und in Datenbank einfügen"
 */
public class CategoryLinksCommand extends Command{

    private static final Logger log = LogManager.getFormatterLogger(Page.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions

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
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get(), batchSize);

        long time = Benchmark.run(() -> {
            graphDb.getAllElements()
                    .map(Page::new)
                    .forEach(Page::insertCategoryLinks);
        });

        log.info(getName() + " took " + time / 1000 + " seconds");
    }
}
