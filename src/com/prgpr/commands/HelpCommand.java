package com.prgpr.commands;

import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.command.CommandBrokerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        List<String> output = new LinkedList<>();

        output.add("-------------------------------------------------");
        output.add("Available Command Arguments");
        output.add("-------------------------------------------------");

        commandBroker.getRegisteredCommands()
                .stream()
                .filter(command -> !Objects.equals(command.getName(), getName()))
                .flatMap(command -> Arrays.stream(command.getArguments()))
                .distinct()
                .map(CommandArgument::getFullDescription)
                .collect(Collectors.toCollection(() -> output));

        output.add("-------------------------------------------------");
        output.add("Available Commands");
        output.add("-------------------------------------------------");

        output.add(getFullDescription());

        commandBroker.getRegisteredCommands()
                .stream()
                .filter(command -> !Objects.equals(command.getName(), getName()))
                .map(Command::getFullDescription)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toCollection(() -> output));


        System.out.println(
                output.stream().collect(Collectors.joining("\n"))
        );
    }
}
