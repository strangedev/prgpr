package com.prgpr.commands.arguments;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.framework.command.CommandArgument;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by kito on 20.11.16.
 *
 * A Command Argument specifying a filepath to an embedded database storage.
 *
 * @author Kyle Rinfreschi
 */
public class DatabaseDirectoryArgument extends CommandArgument {

    @Override
    public String getName() {
        return "DB-Directory";
    }

    @Override
    public String getDescription() {
        return "Directory where the database will store its files.";
    }

    @Override
    public void test(String arg) throws InvalidArgument {
        File f = new File(arg);

        if(!f.exists()){
            throw new InvalidArgument("Database Directory does not exist.");
        }
    }

}
