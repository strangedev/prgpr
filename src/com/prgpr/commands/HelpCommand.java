package com.prgpr.commands;

import com.prgpr.framework.AsciiTable;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.command.CommandBrokerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kito on 19.11.16.
 *
 * Default command.
 * Displays all available commands and their respective syntax.
 *
 * @author Kyle Rinfreschi
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

        System.out.println();  // FIXME dafuq?
    }

    @Override
    public void run() {
        CommandBroker commandBroker = CommandBrokerFactory.getCommandBroker();

        List<String> output = new LinkedList<>();

        // Print all available commands
        output.add("-------------------------------------------------");
        output.add("Commands");
        output.add("-------------------------------------------------");

        AsciiTable t = new AsciiTable();
        t.setColumns("Command", "Arguments", "Description");
        t.addRow(this.getName(), this.getArgumentsAsString(), this.getDescription());

        commandBroker.getRegisteredCommands()
                .stream()
                .filter(command -> !Objects.equals(command.getName(), getName()))                                               // don't display this command
                .sorted((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getName(), c2.getName()))                          // sort by name
                .forEach((command) -> t.addRow(command.getName(), command.getArgumentsAsString(), command.getDescription()));   // add commands to table

        output.add(t.toString());

        // Print all available arguments
        output.add("-------------------------------------------------");
        output.add("Command Arguments");
        output.add("-------------------------------------------------");

        AsciiTable t1 = new AsciiTable();
        t1.setColumns("Argument", "Description");

        commandBroker.getRegisteredCommands()
                .stream()
                .filter(command -> !Objects.equals(command.getName(), getName()))                       // don't display this command
                .filter(command -> command.getArguments() != null)                                      // skip commands with no arguments
                .flatMap(command -> Arrays.stream(command.getArguments()))                              // generate stream of arguments
                .distinct()                                                                             // Remove duplicates
                .sorted((a1, a2) -> String.CASE_INSENSITIVE_ORDER.compare(a1.getName(), a2.getName()))  // sort by name
                .forEach((arg) -> t1.addRow(arg.getName(), arg.getDescription()));                      // add arguments to table

        output.add(t1.toString());

        System.out.println(
                output.stream().collect(Collectors.joining("\n"))  // Join tables to print them
        );
    }
}
