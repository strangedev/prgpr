package com.prgpr.framework.command;

import java.util.Arrays;

/**
 * Created by kito on 19.11.16.
 */
public abstract class Command {

    public abstract String getName();

    public abstract int execute(String[] args);

    public String getDescription(){
        return "";
    }

}
