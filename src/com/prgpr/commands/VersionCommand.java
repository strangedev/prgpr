package com.prgpr.commands;

import com.prgpr.Main;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by kito on 19.11.16.
 * @author Kyle Rinfreschi
 */
public class VersionCommand extends Command {
    private static final Logger log = LogManager.getFormatterLogger(VersionCommand.class);

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "Outputs the current version.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return null;
    }

    @Override
    public void handleArguments(List<String> args) {}

    @Override
    public void run() {
        Properties prop = new Properties();
        try {
            InputStream stream = Main.class.getClassLoader().getResourceAsStream("build.properties");

            if(stream == null)
                System.exit(1);

            //load a properties file from class path, inside static method
            prop.load(stream);

            //get the property value and print it out
            System.out.format("Current version: %s %n", prop.getProperty("version"));
            System.out.format("Last Built: %s %n", prop.getProperty("timestamp"));
        }
        catch (IOException e) {
            log.catching(e);
        }
    }
}
