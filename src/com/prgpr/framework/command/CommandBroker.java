package com.prgpr.framework.command;

import com.prgpr.exceptions.CommandNotFound;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by kito on 19.11.16.
 */
public class CommandBroker {

    private static HashMap<String, Command> commandMap = new HashMap<>();
    private static String defaultCommand = "help";

    public static void register(Command command){
        commandMap.put(command.getName(), command);
    }

    public static void register(Command[] commands){
        Arrays.stream(commands).forEach(
                command -> commandMap.put(command.getName(), command)
        );
    }

    public static void process(String[] args) throws CommandNotFound {
        String commandName = defaultCommand;

        if(args.length > 0){
            commandName = args[0];
        }

        Command command;

        try {
            command = getCommandByName(commandName);
        } catch (CommandNotFound e){
            command = getCommandByName(defaultCommand);
        }

        args = getCommandArgs(command.getName(), args);
        command.execute(args);
    }

    private static String[] getCommandArgs(String commandName, String[] args) {
        if(args.length > 0 && args[0].equals(commandName)) {
            args = Arrays.copyOfRange(args, 1, args.length);
        }

        return args;
    }

    public static void setDefaultCommand(String name){
        defaultCommand = name;
    }

    private static Command getCommandByName(String name) throws CommandNotFound {
        Command command = commandMap.get(name);

        if(command == null){
            throw new CommandNotFound();
        }

        return command;
    }
}
