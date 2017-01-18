package com.prgpr.framework.tasks;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.database.EmbeddedDatabase;

import java.util.List;

/**
 * Created by kito on 13/01/17.
 */
public abstract class Task implements Runnable {
    protected CommandArgument[] arguments = new CommandArgument[0];
    protected EmbeddedDatabase db;

    public abstract String getDescription();
    public abstract String[] getRequirements();
    public abstract String[] produces();

    protected void handleArguments(List<String> arguments) throws InvalidNumberOfArguments, InvalidArgument {}

    public void setArguments(List<String> arguments) throws InvalidArgument, InvalidNumberOfArguments {
        handleArguments(arguments);
    }

    public void setDatabase(EmbeddedDatabase db) {
        this.db = db;
    }
}
