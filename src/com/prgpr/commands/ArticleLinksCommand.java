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
 * Created by lisa on 11/23/16.
 */
public class ArticleLinksCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(Page.class);
    private static final long batchSize = 10000;

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument()
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
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get());

        long time = Benchmark.run(() -> {
            BatchTransaction.run(
                    graphDb,
                    batchSize,
                    graphDb.getAllElements()
                            .map(Page::new)
                            .map(pg -> (Callable<Long>) pg::insertArticleLinks)
                );
            }
        );

        log.info(getName() + " took " + time / 1000 + " seconds");
    }
}

