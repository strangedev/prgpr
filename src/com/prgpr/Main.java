package com.prgpr;

import com.prgpr.commands.CategoryLinksCommand;
import com.prgpr.commands.HelpCommand;
import com.prgpr.commands.ImportHtmlCommand;
import com.prgpr.commands.ResetDBCommand;
import com.prgpr.exceptions.CommandNotFound;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.command.CommandBrokerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Elizaveta Kovalevskaya
 *
 * The Main Class which bootstraps the execution.
 */
public class Main {

    private static final Logger log = LogManager.getFormatterLogger(Main.class);




    /**
     * Creates producer and consumer instances,
     * then subscribes them to each other.
     * Executes the PageFactory to run the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        CommandBroker commandBroker = CommandBrokerFactory.getCommandBroker();

        Command help = new HelpCommand();

        commandBroker.register(new Command[] {
                help,
                new ResetDBCommand(),
                new ImportHtmlCommand(),
                new CategoryLinksCommand()
        });

        try {
            commandBroker.setDefaultCommand(help.getName());
            commandBroker.process(args);
        } catch (CommandNotFound | InvalidNumberOfArguments | InvalidArgument e) {
            e.printStackTrace();
        }
    }
}