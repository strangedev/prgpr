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

import java.util.Collection;
import java.util.stream.BaseStream;

/**
 * Created by lisa on 11/23/16.
 *
 * Command which implements the articlelinks command from Milestone2:
 * From OLAT:
 *
 * "Analog zur Extraktion der Kategorien-Links sollen auch Links zwischen Artikeln
 *  extrahiert und als typisierte Relationships in der Datenbank repräsentiert werden.
 *  Dabei sollen nur solche Links berücksichtigt werden, für welche die Zielseite
 *  auch tatsächlich in der Datenbank vorhanden ist."
 *
 * "Links zwischen Artikeln extrahieren und in Datenbank einfügen"
 *
 * @author Elizaveta Kovalevskaya, Kyle Rinfreschi
 */
public class ArticleLinksCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(Page.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument()
    };

    @Override
    public String getName() {
        return "articlelinks";
    }

    @Override
    public String getDescription() {
        return "Extracts links between articles.";
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
            try {
                graphDb.getAllElements()
                        .map(Page::new)
                        .map((page) -> {
                            log.info("------------------------------------");
                            log.info("Creating relationships of type Article for " + page.getTitle());
                            log.info("------------------------------------");
                            return page.insertArticleLinks();
                        })
                        .flatMap(BaseStream::sequential)
                        .forEach(log::info);
            } catch (Exception e){
                log.catching(e);
            }
        });

        log.info(getName() + "command took " + time / 1000 + " seconds");
    }
}

