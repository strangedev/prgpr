package com.prgpr.framework.command;

/**
 * Created by kito on 19.11.16.
 */
public abstract class Command {

    public abstract String getName();

    public abstract void execute(String[] args);

    public String getDescription(){
        return "";
    }

}
