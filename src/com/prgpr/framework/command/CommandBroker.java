package com.prgpr.framework.command;

import com.prgpr.exceptions.CommandNotFoundException;
import com.prgpr.exceptions.InvalidArgumentsException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by kito on 19.11.16.
 */
public class CommandBroker {

    private HashMap<String, Command> commandMap = new HashMap<>();
    private Command defaultCommand;

    public void register(Command command){
        commandMap.put(command.getName().toLowerCase(), command);
    }

    public void register(Command[] commands){
        Arrays.stream(commands).forEach(this::register);
    }

    public void setDefaultCommand(String name) throws CommandNotFoundException {
        defaultCommand = getCommandByName(name);
    }

    public Collection<Command> getRegisteredCommands(){
        return commandMap.values();
    }

    public void process(String[] args) throws CommandNotFoundException, InvalidArgumentsException {
        if(args.length == 0){
            executeDefaultCommand(args);
            return;
        }

        try {
            Command command = getCommandByName(args[0]);
            command.execute(Arrays.copyOfRange(args, 1, args.length));
        } catch (CommandNotFoundException | InvalidArgumentsException e){
            executeDefaultCommand(args);
        }
    }

    private void executeDefaultCommand(String[] args) throws InvalidArgumentsException, CommandNotFoundException {
        if(defaultCommand == null){
            throw new CommandNotFoundException();
        }

        defaultCommand.execute(args);
    }

    private Command getCommandByName(String name) throws CommandNotFoundException {
        Command command = commandMap.get(name.toLowerCase());

        if(command == null){
            throw new CommandNotFoundException();
        }

        return command;
    }
}
