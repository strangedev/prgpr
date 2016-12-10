package com.prgpr;

import com.prgpr.commands.*;
import com.prgpr.exceptions.CommandNotFound;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.command.CommandBrokerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        CommandBroker commandBroker = CommandBrokerFactory.getCommandBroker();

        Command help = new HelpCommand();

        // Initializes the possible commands which can be executed.
        commandBroker.register(new Command[] {
                help,
                new ResetDBCommand(),
                new ImportHtmlCommand(),
                new CategoryLinksCommand(),
                new ArticleLinksCommand(),
                new PageInfoCommand(),
                new VersionCommand()
        });

        try {
            commandBroker.setDefaultCommand(help.getName());
            commandBroker.process(args);
        } catch (CommandNotFound | InvalidNumberOfArguments | InvalidArgument e) {
            log.catching(e);
        }
    }
}