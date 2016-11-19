package com.prgpr;

import com.prgpr.commands.HelpCommand;
import com.prgpr.commands.ImportHtmlCommand;
import com.prgpr.data.Page;
import com.prgpr.exceptions.CommandNotFound;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.command.CommandBroker;
import com.prgpr.framework.database.TransactionManager;
import com.prgpr.framework.threading.ThreadManager;
import com.prgpr.helpers.Benchmark;
import com.prgpr.helpers.ProducerLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Elizaveta Kovalevskaya
 *
 * The Main Class which bootstraps the execution.
 */
public class Main {

    private static final Logger log = LogManager.getFormatterLogger(Main.class);




    /**
     * Creates producer and consumer instances,
     * then subscribes them to each other.
     * Executes the PageFactory to run the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        CommandBroker.register(new Command[]{
                new HelpCommand(),
                new ImportHtmlCommand()
        });

        try {
            CommandBroker.process(args);
        } catch (CommandNotFound commandNotFound) {
            commandNotFound.printStackTrace();
        }
    }
}