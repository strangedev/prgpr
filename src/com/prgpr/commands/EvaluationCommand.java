package com.prgpr.commands;

import com.prgpr.EntityFinder;
import com.prgpr.Evaluation;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.commands.arguments.HtmlInputFileArgument;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by strange on 1/18/17.
 */
public class EvaluationCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(CreateDBCommand.class);

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument(),
            new HtmlInputFileArgument()
    };

    @Override
    public String getName() {
        return "evaluation";
    }

    @Override
    public String getDescription() {
        return "Runs the evaluation required by milestone 3";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
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
        Evaluation e = new Evaluation();
        e.run();
    }
}
