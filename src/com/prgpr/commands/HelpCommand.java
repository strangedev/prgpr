package com.prgpr.commands;

import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandBroker;

import java.util.Arrays;

/**
 * Created by kito on 19.11.16.
 */
public class HelpCommand extends Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public int execute(String[] args) {
        if(args.length > 0)
            System.out.println("Executed command: " + args[0]);

        CommandBroker.getRegisteredCommands().forEach(
                (command) -> {
                    System.out.println(command.getName() + " - " + command.getDescription());
                }
        );
        return 0;
    }
}
