package com.prgpr.framework.command;

import com.prgpr.exceptions.CommandNotFound;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kito on 19.11.16.
 */
public class CommandBroker {

    private static HashMap<String, Command> commandMap = new HashMap<>();
    private static Command defaultCommand;

    public static void register(Command command){
        commandMap.put(command.getName().toLowerCase(), command);
    }

    public static void register(Command[] commands){
        Arrays.stream(commands).forEach(CommandBroker::register);
    }

    public static void setDefaultCommand(String name) throws CommandNotFound {
        defaultCommand = getCommandByName(name);
    }

    public static Collection<Command> getRegisteredCommands(){
        return commandMap.values();
    }

    public static void process(String[] args) throws CommandNotFound {
        Command command = defaultCommand;

        if(args.length == 0 && defaultCommand == null){
            throw new CommandNotFound();
        }

        if(args.length == 0){
            command.execute(new String[]{});
            return;
        }

        try {
            command = getCommandByName(args[0]);
            args = Arrays.copyOfRange(args, 1, args.length);
        } catch (CommandNotFound e){
            // do nothing
        }

        if(command == null){
            throw new CommandNotFound();
        }

        command.execute(args);
    }

    private static Command getCommandByName(String name) throws CommandNotFound {
        Command command = commandMap.get(name.toLowerCase());

        if(command == null){
            throw new CommandNotFound();
        }

        return command;
    }
}
