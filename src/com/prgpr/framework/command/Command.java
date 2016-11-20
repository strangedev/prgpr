package com.prgpr.framework.command;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;

/**
 * Created by kito on 19.11.16.
 */
public abstract class Command implements Runnable {

    protected CommandArgument[] arguments;

    public abstract String getName();

    public String getDescription(){
        return "";
    }

    public CommandArgument[] getArgumentsList(){
        return this.arguments;
    }

    public String getFullDescription(){
        return getName() + ": " + getDescription();
    }

    public void handleArguments(String[] args) throws InvalidNumberOfArguments, InvalidArgument {}

    public void execute(String[] args) throws InvalidNumberOfArguments, InvalidArgument {
        handleArguments(args);
        run();
    }

    public void execute() throws InvalidNumberOfArguments, InvalidArgument {
        handleArguments(new String[]{});
        run();
    }

}
