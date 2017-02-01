package com.prgpr.commands;

import com.prgpr.EntityFinder;
import com.prgpr.PageFinder;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.framework.restapi.Router;

import java.util.List;

/**
 * Created by strange on 1/20/17.
 * @author Noah Hummel
 */
public class RunServerCommand extends Command {

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument()
    };

    @Override
    public String getName() {
        return "runserver";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
    }

    @Override
    public String getDescription() {
        return "Starts the server serving the REST API.";
    }

    @Override
    public void handleArguments(List<String> args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.size() < 1){
            throw new InvalidNumberOfArguments();
        }
        arguments[0].set(args.get(0));
    }

    @Override
    public void run() {
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get());
        EntityFinder.setDatabase(graphDb);
        PageFinder.setDatabase(graphDb);
        Router.setDatabase(graphDb);
        Router.run();
    }
}
