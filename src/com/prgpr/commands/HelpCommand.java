package com.prgpr.commands;

import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.command.CommandBrokerFactory;

import java.util.Objects;

/**
 * Created by kito on 19.11.16.
 */
public class HelpCommand extends Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public CommandArgument[] getArguments() {
        return null;
    }

    @Override
    public void handleArguments(String[] args) {
        if(args.length == 0)
            return;

        System.out.println("Failed to execute command: '" + args[0] + "'");
        System.out.println("Reason: " + args[1]);
        if(args[2] != null) {
            System.out.println('\t' + args[2]);
        }

        System.out.println();
    }

    @Override
    public void run() {
        CommandBroker commandBroker = CommandBrokerFactory.getCommandBroker();

        System.out.println("-------------------------------------------------");
        System.out.println("Available Commands");
        System.out.println("-------------------------------------------------");
        System.out.println(getFullDescription());
        commandBroker.getRegisteredCommands()
                .stream()
                .filter(command -> !Objects.equals(command.getName(), getName()))
                .map(Command::getFullDescription)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .forEach(System.out::println);
    }
}
