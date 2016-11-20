package com.prgpr.commands;

import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.command.CommandBrokerFactory;

/**
 * Created by kito on 19.11.16.
 */
public class HelpCommand extends Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void handleArguments(String[] args) {
        if(args.length > 0)
            System.out.println("Command " + args[0] + " does not exist.");
    }

    @Override
    public void run() {
        CommandBroker commandBroker = CommandBrokerFactory.getCommandBroker();

        commandBroker.getRegisteredCommands().forEach(
                (command) -> System.out.println(command.getFullDescription())
        );
    }
}
