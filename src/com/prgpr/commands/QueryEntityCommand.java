package com.prgpr.commands;

import com.prgpr.EntityFinder;
import com.prgpr.commands.arguments.ArticleTitleArgument;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.data.City;
import com.prgpr.data.EntityBase;
import com.prgpr.data.Monument;
import com.prgpr.data.Person;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;;
import java.util.List;
import java.util.Set;

/**
 * @author Kyle Rinfreschi
 */
public class QueryEntityCommand extends Command{

    private static final Logger log = LogManager.getFormatterLogger(QueryEntityCommand.class);

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument(),
            new ArticleTitleArgument()
    };

    @Override
    public String getName() {
        return "queryentity";
    }

    @Override
    public String getDescription() {
        return "Queries for an entity given it's name.";
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
        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(arguments[0].get());
        EntityFinder.setDatabase(graphDb);

        Set<EntityBase> entities = EntityFinder.getEntitiesByPageTitle(arguments[1].get());

        entities.forEach(System.out::print);
    }

}
