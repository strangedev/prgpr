package com.prgpr.commands;

import com.prgpr.framework.command.Command;

/**
 * Created by kito on 19.11.16.
 */
public class HelpCommand implements Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public int execute(String[] args) {
        System.out.println(args[0]);
        return 0;
    }
}
