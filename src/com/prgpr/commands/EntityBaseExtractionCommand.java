package com.prgpr.commands;

import com.prgpr.PageFinder;
import com.prgpr.TypeExtraction;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.data.*;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.helpers.Benchmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.prgpr.PageFinder.findAllByNamespace;

/**
 * Command which implements the entitybaseextraction and inserts the entity links command from Milestone3
 *
 * @author Elizaveta Kovalevskaya
 */
public class EntityBaseExtractionCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(EntityBaseExtractionCommand.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument()
    };

    @Override
    public String getName() {
        return "entitybaseextraction";
    }

    @Override
    public String getDescription() {
        return "Extracts entities";
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

    public void insertEntityBaseLinks(Set<EntityBase> toAdd) {
        for (EntityBase adding : toAdd) {
                log.info("------------------------------------------");
                log.info("Creating relationships of type Entity for " + adding.getTitle());
                log.info("------------------------------------------");
                adding.insertEntityLinks().forEach(log::info);
            }
    }


    @Override
    public void run() {
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get(), batchSize);
        PageFinder.setDatabase(graphDb);

        long time = Benchmark.run(() -> {
            try {
                Set<Page> pages = findAllByNamespace(0);
                Set<EntityBase> entities = new LinkedHashSet<>();
                for (Page page: pages) {

                    log.info("------------------------------------------");
                    Set<Page> entityTypes = TypeExtraction.discoverTypes(page);
                    for (Page entity : entityTypes) {

                        if (entity.getTitle().equals("Person")) {
                            entities.add(new Person(graphDb, page));
                            log.info("Person " + page.getTitle() + " has been added.");
                        }
                        if (entity.getTitle().equals("Ort")) {
                            entities.add(new City(graphDb, page));
                            log.info("City " + page.getTitle() + " has been added.");
                        }
                        if (entity.getTitle().equals("Denkmal")) {
                            entities.add(new Monument(graphDb, page));
                            log.info("Monument " + page.getTitle() + " has been added.");
                        }
                    }
                }
                insertEntityBaseLinks(entities);
            } catch (Exception e){
                log.catching(e);
            }
        });
        try {
            graphDb.success();
        } catch (NotInTransactionException e) {
            e.printStackTrace();
        }
        log.info(getName() + " took " + time / 1000 + " seconds");
    }
}

