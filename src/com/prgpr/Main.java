package com.prgpr;

import com.prgpr.commands.*;
import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.exceptions.CommandNotFound;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.command.CommandBrokerFactory;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;
import com.prgpr.framework.tasks.TaskScheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * The Main Class which bootstraps the execution.
 *
 * @author Elizaveta Kovalevskaya
 */
public class Main {

    private static final Logger log = LogManager.getFormatterLogger(Main.class);

    /**
     * Provides an entry point for the Command Pattern and starts the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        log.info("WikiXtractor has started!");

        /*
            WikiXtractor <Database-Directory> createdb <HTML-Input-File>
            WikiXtractor <Database-Directory> executetasks <Task-File>
            WikiXtractor <Database-Directory> queryentity <Artikel-Title>
        */

        CommandBroker commandBroker = CommandBrokerFactory.getCommandBroker();

        Command help = new HelpCommand();

        // Initializes the possible commands which can be executed.
        commandBroker.register(new Command[] {
                help,
                new VersionCommand(),
                new CreateDBCommand(),
                new ExecuteTasksCommand(),
                new QueryEntityCommand(),
                new TestDBCommand(),
                new EvaluationCommand()
        });

        try {
            commandBroker.setDefaultCommand(help.getName());
            commandBroker.process(args);
        } catch (CommandNotFound | InvalidNumberOfArguments | InvalidArgument e) {
            log.catching(e);
        }
    }
}