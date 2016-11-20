package com.prgpr.framework.command;

import com.prgpr.exceptions.InvalidArgumentsException;

/**
 * Created by kito on 19.11.16.
 */
public abstract class Command implements Runnable {

    public abstract String getName();

    public String getDescription(){
        return "";
    }

    public String getFullDescription(){
        return getName() + ": " + getDescription();
    }

    public void handleArguments(String[] args) throws InvalidArgumentsException {}

    public void execute(String[] args) throws InvalidArgumentsException {
        handleArguments(args);
        run();
    }

    public void execute() throws InvalidArgumentsException {
        handleArguments(new String[]{});
        run();
    }

}
