package com.prgpr.commands.arguments;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.framework.command.CommandArgument;

import java.io.File;

/**
 * Created by kito on 20.11.16.
 *
 * A command Argument specifying a filepath to an html file.
 *
 * @author Kyle Rinfreschi
 */
public class TaskFileArgument extends CommandArgument {

    @Override
    public String getName() {
        return "Task-File";
    }

    @Override
    public String getDescription() {
        return "File that contains a list of tasks to be executed.";
    }

    @Override
    public void test(String arg) throws InvalidArgument {
        File f = new File(arg);

        if(!f.exists()){
            throw new InvalidArgument("Tasks file does not exist.");
        }
    }
}
