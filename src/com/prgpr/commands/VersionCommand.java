package com.prgpr.commands;

import com.prgpr.Main;
import com.prgpr.framework.AsciiTable;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.command.CommandBrokerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kyle Rinfreschi
 * Created by kito on 19.11.16.
 */
public class VersionCommand extends Command {
    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "Returns the current version.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return null;
    }

    @Override
    public void handleArguments(String[] args) {}

    @Override
    public void run() {
        Properties prop = new Properties();
        try {
            //load a properties file from class path, inside static method
            prop.load(Main.class.getClassLoader().getResourceAsStream("build.properties"));

            //get the property value and print it out
            System.out.format("Current version: %s %n", prop.getProperty("version"));
            System.out.format("Last Built: %s %n", prop.getProperty("timestamp"));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
