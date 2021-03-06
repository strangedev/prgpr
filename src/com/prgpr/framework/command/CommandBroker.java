package com.prgpr.framework.command;

import com.prgpr.exceptions.CommandNotFound;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kito on 19.11.16.
 *
 * A class managing a set of Commands, which is capable of choosing and executing the matching command
 * for a given command line input.
 * Also has the capability to provide a fallback Command to be executed when no matching command was found.
 *
 * @author Kyle Rinfreschi
 */
public class CommandBroker {

    private HashMap<String, Command> commandMap = new HashMap<>();  // Maps command name to instance of command
    private Command defaultCommand;  // As a fallback option when no command matches

    /**
     * Adds a new command to the set of known commands. If a command exists already, it'll be overwritten.
     *
     * @param command An instance of a new command to be added.
     */
    public void register(Command command){
        commandMap.put(command.getName().toLowerCase(), command);
    }

    /**
     * Adds an array of new commands to the set of known commands. If a command exists already, it'll be overwritten.
     *
     * @param commands A list of instances of new commands to be added.
     */
    public void register(Command[] commands){
        Arrays.stream(commands).forEach(this::register);
    }

    /**
     * Sets one of the registered, known commands as the default command to be executed, when no matching command
     * is found. The Command has to be registered before it can be set as default command.
     *
     * @param name The name of the command to set as default command
     * @throws CommandNotFound If the command is not known to the command broker
     */
    public void setDefaultCommand(String name) throws CommandNotFound {
        defaultCommand = findCommandFromArgs(name);
    }

    /**
     * @return A collection of instances of all known and registered commands
     */
    public Collection<Command> getRegisteredCommands(){
        return commandMap.values();
    }

    /**
     * Processes the command line input array usually passed to main() by the jvm.
     * Chooses one of the known, registered commands to be executed based on the command name
     * and tries to execute it.
     *
     * @param args An array of command line parameters as strings.
     * @throws CommandNotFound When the command name is not recognized and no default command was set.
     * @throws InvalidNumberOfArguments  A wrong number of arguments was passed to the default command.
     * @throws InvalidArgument A malformed argument was passed to the default command.
     */
    public void process(String[] args) throws CommandNotFound, InvalidNumberOfArguments, InvalidArgument {
        if(args.length == 0){  // If no command is specified, use the default command.
            executeDefaultCommand(Collections.emptyList());
            return;
        }

        try {
            Command command = findCommandFromArgs(args);
            List<String> cmdArgs = Arrays.stream(args)
                                        .filter(arg -> !arg.equals(command.getName()))
                                        .collect(Collectors.toList());

            command.execute(cmdArgs);
        } catch (CommandNotFound | InvalidNumberOfArguments | InvalidArgument e){
            if(defaultCommand == null){
                throw e;
            }

            List<String> cmdArgs = Arrays.asList(
                    Arrays.stream(args).reduce((s1, s2) -> s1 + " " + s2).orElse(""),
                    e.getClass().getSimpleName(),
                    e.getMessage()
            );

            executeDefaultCommand(cmdArgs);  // if an error occurred, execute the default command
        }
    }

    /**
     * Executes the default command with the given arguments.
     *
     * @param args An array of command line parameters as strings.
     * @throws CommandNotFound When no default command was set.
     * @throws InvalidNumberOfArguments When a wrong number of arguments was passed.
     * @throws InvalidArgument When a malformed argument was passed.
     */
    private void executeDefaultCommand(List<String> args) throws InvalidNumberOfArguments, CommandNotFound, InvalidArgument {
        if(defaultCommand == null){
            throw new CommandNotFound("default");
        }

        defaultCommand.execute(args);
    }

    private Command findCommandFromArgs(String name) {
        return findCommandFromArgs(new String[]{name});
    }

    /**
     * Looks up a command by it's name.
     *
     * @param args The name of the command.
     * @return The instance of the command.
     * @throws CommandNotFound When the command is not registered.
     */
    private Command findCommandFromArgs(String[] args) throws CommandNotFound {
        Command command = null;

        // Iterate through arguments to support new required syntax
        for(String arg : args){
            command = commandMap.get(arg.toLowerCase());

            if(command != null)
                break;
        }

        if (command == null) {
            throw new CommandNotFound();
        }

        return command;
    }
}
