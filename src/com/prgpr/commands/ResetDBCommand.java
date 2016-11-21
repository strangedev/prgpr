package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectory;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.DatabaseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by lisa on 11/21/16.
 */
public class ResetDBCommand extends Command {


    private static final Logger log = LogManager.getFormatterLogger(Page.class);

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectory()
    };

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Resets the database in the given argument and generates a new empty database.";
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
        File dbf = new File(arguments[0].get());
        if (dbf.exists()) {
            try {
                FileUtils.deleteRecursively(dbf);
                log.info("Database reset finished.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}