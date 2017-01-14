package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.commands.arguments.HtmlInputFileArgument;
import com.prgpr.data.Person;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.Element;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskScheduler;
import com.prgpr.helpers.Benchmark;
import com.prgpr.tasks.ArticleLinkExtraction;
import com.prgpr.tasks.CategoryLinkExtraction;
import com.prgpr.tasks.EntityBaseExtraction;
import com.prgpr.tasks.HTMLDumpImport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by lissie on 1/10/17.
 */
public class CreateDBCommand extends Command{

    private static final Logger log = LogManager.getFormatterLogger(CreateDBCommand.class);


    protected final CommandArgument[] arguments = new CommandArgument[]{
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
    public void handleArguments(String[] args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.length < 1){
            throw new InvalidNumberOfArguments();
        }

        arguments[0].set(args[0]);
    }

    @Override
    public void run() {
        TaskScheduler scheduler = new TaskScheduler(arguments);
        scheduler.register(new Task[]{
                new CategoryLinkExtraction(),
                new EntityBaseExtraction(),
                new ArticleLinkExtraction(),
                new HTMLDumpImport(),
        });
        scheduler.run();
    }

}
