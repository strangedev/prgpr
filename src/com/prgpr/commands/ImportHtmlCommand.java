package com.prgpr.commands;

import com.prgpr.PageFactory;
import com.prgpr.PageProducer;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.commands.arguments.HtmlInputFileArgument;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.helpers.Benchmark;
import com.prgpr.helpers.ProducerLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Kyle Rinfreschi
 * Created by kito on 19.11.16.
 */
public class ImportHtmlCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(Command.class);

    protected static final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument(),
            new HtmlInputFileArgument()
    };

    @Override
    public String getName() {
        return "importhtml";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
    }

    @Override
    public String getDescription() {
        return "Imports the HTML-File into the database.";
    }

    @Override
    public void handleArguments(String[] args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.length < 2){
            throw new InvalidNumberOfArguments();
        }

        arguments[0].set(args[0]);
        arguments[1].set(args[1]);
    }

    @Override
    public void run() {
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get(), 500);

        PageFactory.setDatabase(graphDb);

        PageProducer pageProducer = new PageProducer(arguments[1].get());

        // Logging units
        //ProducerLogger<Page> pageFactoryLogger = new ProducerLogger<>(true);
        //pageFactoryLogger.subscribeTo(pageProducer);

        // Execute
        long timeImport = Benchmark.run(pageProducer);

        long timeCount = Benchmark.run(()->{
            log.info(graphDb.getAllElements().count());
        });

        log.info("Import took: " + (timeImport / 1000) + " seconds");
        log.info("Counted all nodes in: " + (timeCount / 1000.0) + " seconds");
    }
}
