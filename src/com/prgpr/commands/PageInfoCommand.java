package com.prgpr.commands;

import com.prgpr.commands.arguments.DatabaseDirectoryArgument;
import com.prgpr.commands.arguments.NamespaceIDArgument;
import com.prgpr.commands.arguments.PageTitleArgument;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;

/**
 * Created by kito on 02.12.16.
 */
public class PageInfoCommand extends Command {

    protected static final CommandArgument[] arguments = new CommandArgument[]{
            new DatabaseDirectoryArgument(),
            new NamespaceIDArgument(),
            new PageTitleArgument()
    };

    @Override
    public String getName() {
        return "pageinfo";
    }

    @Override
    public CommandArgument[] getArguments() {
        return arguments;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void run() {

    }
}
