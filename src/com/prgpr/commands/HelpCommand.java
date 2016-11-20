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
    public void execute(String[] args) {
        if(args.length > 0)
            System.out.println("Executed command: " + args[0]);

        CommandBroker commandBroker = CommandBrokerFactory.getCommandBroker();

        commandBroker.getRegisteredCommands().forEach(
                (command) -> {
                    System.out.println(command.getName() + " - " + command.getDescription());
                }
        );
    }
}