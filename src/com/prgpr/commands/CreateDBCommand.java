package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.commands.arguments.HtmlInputFileArgument;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskScheduler;
import com.prgpr.tasks.ArticleLinkExtraction;
import com.prgpr.tasks.CategoryLinkExtraction;
import com.prgpr.tasks.EntityBaseExtraction;
import com.prgpr.tasks.HTMLDumpImport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lissie on 1/10/17.
 */
public class CreateDBCommand extends Command{

    private static final Logger log = LogManager.getFormatterLogger(CreateDBCommand.class);

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument(),
            new HtmlInputFileArgument()
    };

    @Override
    public String getName() {
        return "createdb";
    }

    @Override
    public String getDescription() {
        return "creates a database given an html input file";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
    }

    @Override
    public void handleArguments(List<String> args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.size() < 2){
            throw new InvalidNumberOfArguments();
        }

        arguments[0].set(args.get(0));
        arguments[1].set(args.get(1));
    }

    @Override
    public void run() {
        try {
            FileUtils.deleteRecursively(new File(arguments[0].get()));
        } catch (IOException e) {
            log.catching(e);
        }

        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get());
        TaskScheduler.setDatabase(graphDb);

        TaskScheduler scheduler = new TaskScheduler();
        scheduler.register(new Task[]{
                new CategoryLinkExtraction(),
                new EntityBaseExtraction(),
                new ArticleLinkExtraction(),
        });

        HTMLDumpImport htmlDumpImport = new HTMLDumpImport();

        htmlDumpImport.setArguments(Collections.singletonList(arguments[1].get()));

        scheduler.register(htmlDumpImport);

        scheduler.run();
    }

}
