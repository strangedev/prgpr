package com.prgpr.commands;

import com.prgpr.PageFinder;
import com.prgpr.TypeExtraction;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.data.EntityBase;
import com.prgpr.data.Page;
import com.prgpr.data.Person;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.helpers.Benchmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;

import static com.prgpr.PageFinder.findAllByNamespace;

/**
 * Created by lissie on 1/5/17.
 */
public class EntityBaseExtractionCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(Page.class);
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
        toAdd
            .stream()
            .map((adding) -> {
                log.info("------------------------------------");
                log.info("Creating relationships of type Entity for " + adding.getTitle());
                log.info("------------------------------------");
                return adding.insertEntityLinks();
            })
            .flatMap(BaseStream::sequential)
            .forEach(log::info);
    }


    @Override
    public void run() {
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get(), batchSize);
        PageFinder.setDatabase(graphDb);

        long time = Benchmark.run(() -> {
            try {
                Set<Page> pages = findAllByNamespace(0);
                log.info(pages);
                Set<EntityBase> entities = new LinkedHashSet<>();
                pages.stream().map( (page) -> {
                    Set<Page> entityTypes = TypeExtraction.discoverTypes(page);
                    entityTypes.stream().map( (entity) ->
                    {
                        if (entity.getTitle().equals("Person")) {
                            log.info("Person " + page.getTitle() + " has been added.");
                            Person p = new Person(graphDb, page);
                            entities.add(p);
                            return p;
                        }
                        /*
                        else if (entity.getTitle().equals("Ort")) {
                            log.info("City " + page.getTitle() + " has been added.");
                            City c = new City(graphDb, page);
                            entities.add(c);
                            return c;
                        }
                        else if (entity.getTitle().equals("Denkmal")) {
                            log.info("Monument " + page.getTitle() + " has been added.");
                            Monument m = new Monument(graphDb, page);
                            entities.add(m);
                            return m;
                        */
                        else { return null; }
                    });
                    return entityTypes;
                });
                insertEntityBaseLinks(entities);
            } catch (Exception e){
                log.catching(e);
            }
        });

        log.info(getName() + " took " + time / 1000 + " seconds");
    }
}

