package com.prgpr.framework.command;

import com.prgpr.exceptions.CommandNotFound;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;

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

    public void setDefaultCommand(String name) throws CommandNotFound {
        defaultCommand = getCommandByName(name);
    }

    public Collection<Command> getRegisteredCommands(){
        return commandMap.values();
    }

    public void process(String[] args) throws CommandNotFound, InvalidNumberOfArguments, InvalidArgument {
        if(args.length == 0){
            executeDefaultCommand(args);
            return;
        }

        try {
            Command command = getCommandByName(args[0]);
            command.execute(Arrays.copyOfRange(args, 1, args.length));
        } catch (CommandNotFound | InvalidNumberOfArguments | InvalidArgument e){
            args = new String[] {
                    Arrays.stream(args).reduce((s1, s2) -> s1 + " " + s2).orElse(""),
                    e.getClass().getSimpleName(),
                    e.getMessage()
            };
            executeDefaultCommand(args);
        }
    }

    private void executeDefaultCommand(String[] args) throws InvalidNumberOfArguments, CommandNotFound, InvalidArgument {
        if(defaultCommand == null){
            throw new CommandNotFound();
        }

        defaultCommand.execute(args);
    }

    private Command getCommandByName(String name) throws CommandNotFound {
        Command command = commandMap.get(name.toLowerCase());

        if(command == null){
            throw new CommandNotFound();
        }

        return command;
    }
}
