package com.prgpr.commands.arguments;

import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.CommandArgument;

import java.io.File;

/**
 * Created by kito on 20.11.16.
 */
public class HtmlInputFile extends CommandArgument {

    @Override
    public String getName() {
        return "HTML-Input-File";
    }

    @Override
    public void test(String arg) throws InvalidArgument {
        File f = new File(arg);

        if(!f.exists()){
            throw new InvalidArgument("Html file does not exist.");
        }
    }
}