package com.prgpr.commands;

import com.prgpr.PageFinder;
import com.prgpr.TypeExtraction;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.data.*;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.prgpr.PageFinder.findAllByNamespace;

/**
 * Created by lissie on 1/10/17.
 */
public class EntityTest extends Command{

    private static final Logger log = LogManager.getFormatterLogger(com.prgpr.commands.EntityBaseExtractionCommand.class);
    private static final int batchSize = 10000;  // Specifies the batch size for batched transactions
    private static final String stringHashFunction = "SHA-1";


    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument()
    };

    @Override
    public String getName() {
        return "entitytest";
    }

    @Override
    public String getDescription() {
        return "Tests if entity is in database.";
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

    protected static int hashCode(String title, int namespaceID) {
        byte[] titleHash;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(stringHashFunction);
            messageDigest.update(title.getBytes());
            titleHash = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            titleHash = title.getBytes();
        }

        return (namespaceID * 31) + Arrays.hashCode(titleHash);
    }


    @Override
    public void run() {
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get());

        long time = Benchmark.run(() -> {
            try {

                Element el = graphDb.getNodeFromIndex("Persons", hashCode("Shivaji", 16));
                log.info(el.toString());
                Person eli = new Person(el);
                log.info(eli.getLinkedEntities().stream().map((blub) -> blub.getTitle()).collect(Collectors.toSet()).toString());

                log.info(eli.getLinkingEntities().stream().map((blub) -> blub.getTitle()).collect(Collectors.toSet()).toString());

            } catch (Exception e){
                log.catching(e);
            }
        });

        log.info(getName() + " took " + time / 1000 + " seconds");
    }

}
