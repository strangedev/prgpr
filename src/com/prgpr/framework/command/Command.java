package com.prgpr.framework.command;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by kito on 19.11.16.
 *
 * An abstract superclass for a Command design pattern.
 * A command has a name, a number of arguments and can be run.
 * Commands are managed by a command broker, which can decide which command to run given a certain command line input.
 *
 * @author Kyle Rinfreschi
 */
public abstract class Command implements Runnable {

    /**
     * @return The name of the command.
     */
    public abstract String getName();

    /**
     * @return The required arguments that have to be passed to this command in order to run it.
     */
    public abstract CommandArgument[] getArguments();

    /**
     * @return A human readable description of the command, used to display help messages and list all of the available
     * commands in a readable fashion by the CommandBroker.
     */
    public abstract String getDescription();

    public List<CommandArgument> getArgumentsList(){
        CommandArgument[] arguments = getArguments();

        if(arguments == null){
            return Collections.emptyList();
        }

        return Arrays.asList(arguments);
    }

    @Override
    public String toString() {
        String argsStr = getArgumentsList().stream()
                .map(CommandArgument::toString)
                .reduce((a1, a2) -> String.format("%s %s", a1, a2)).orElse(null);

        if(argsStr == null)
            return getName();

        return getName() + " " + argsStr;
    }

    /**
     * Method used to pass arguments as strings to the command. The command will either accept the given commands or
     * throw an exception if the arguments are invalid. Must be run before the command can be run, if the command takes
     * any arguments.
     * By default, this method is called by execute before running the command.
     *
     * @param args The arguments to be passed to the command as strings.
     * @throws InvalidNumberOfArguments When an incorrect number of arguments was given.
     * @throws InvalidArgument When an incorrect type of arguments was given.
     */
    public void handleArguments(List<String> args) throws InvalidNumberOfArguments, InvalidArgument {}

    /**
     * Method used to run the command.
     * Even though Command implements runnable, it should not be run directly, but using this method, because it will
     * delegate the task of handling any given arguments to the handleArguments() method.
     * After the arguments are handled by the command, the command will be run.
     *
     * @param args The arguments to be passed to the command as strings.
     * @throws InvalidNumberOfArguments When an incorrect number of arguments was given.
     * @throws InvalidArgument When an incorrect type of arguments was given.
     */
    public void execute(List<String> args) throws InvalidNumberOfArguments, InvalidArgument {
        handleArguments(args);
        run();
    }

    /**
     * Method used to run the command.
     * Even though Command implements runnable, it should not be run directly, but using this method, because it will
     * delegate the task of handling any given arguments to the handleArguments() method.
     * After the arguments are handled by the command, the command will be run.
     * This version of the method takes no argument and is intended to be used with commands which don't take any
     * arguments.
     * The exceptions thrown by this method are only thrown, if the command takes arguments, but none were provided.
     *
     * @throws InvalidNumberOfArguments When an incorrect number of arguments was given.
     * @throws InvalidArgument When an incorrect type of arguments was given.
     */
    public void execute() throws InvalidNumberOfArguments, InvalidArgument {
        handleArguments(Collections.emptyList());
        run();
    }

}
