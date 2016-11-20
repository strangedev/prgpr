package com.prgpr.framework.command;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by kito on 19.11.16.
 */
public abstract class Command implements Runnable {

    public abstract String getName();

    public abstract CommandArgument[] getArguments();

    public abstract String getDescription();

    public List<CommandArgument> getArgumentsList(){
        CommandArgument[] arguments = getArguments();

        if(arguments == null){
            return Collections.emptyList();
        }

        return Arrays.asList(arguments);
    }

    public String getArgumentsAsString(){
        return getArgumentsList().stream()
                .map((arg) -> String.format("<%s>", arg.getName()))
                .reduce((a1, a2) -> String.format("%s %s", a1, a2)).orElse(null);
    }

    public String getFullDescription(){
        String name = getName();
        String args = getArgumentsAsString();
        String desc = getDescription();

        if(args == null && desc == null){
            return name;
        }

        if(args == null){
            return String.format("%s : %s", name, desc);
        }

        if(desc == null){
            return String.format("%s %s", name, args);
        }

        return String.format("%s %s : %s", name, args, desc);
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
