package com.prgpr.commands;

import com.prgpr.PageFactory;
import com.prgpr.PageProducer;
import com.prgpr.data.Page;
import com.prgpr.exceptions.InvalidArgumentsException;
import com.prgpr.framework.command.Command;
import com.prgpr.framework.database.DatabaseFactory;
import com.prgpr.helpers.Benchmark;
import com.prgpr.helpers.ProducerLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * Created by kito on 19.11.16.
 */
public class ImportHtmlCommand extends Command {

    private static final Logger log = LogManager.getFormatterLogger(Command.class);

    private String dbPath;
    private String infilePath;

    @Override
    public String getName() {
        return "importhtml";
    }

    @Override
    public String getDescription() {
        return "Imports an html file into the database.";
    }

    @Override
    public void handleArguments(String[] args) throws InvalidArgumentsException {
        if(args.length < 2){
            throw new InvalidArgumentsException();
        }

        dbPath = args[0];
        infilePath = args[1];
    }

    @Override
    public void run() {
        GraphDatabaseService graphDb = DatabaseFactory.newEmbeddedDatabase(dbPath);
        PageFactory.setDatabase(graphDb);

        PageProducer pageProducer = new PageProducer(infilePath);

        // Logging units
        ProducerLogger<Page> pageFactoryLogger = new ProducerLogger<>(true);
        pageFactoryLogger.subscribeTo(pageProducer);

        // Execute
        pageProducer.run();

        long time = Benchmark.run(()->{
            try ( Transaction tx = graphDb.beginTx() ) {
                log.info(graphDb.getAllNodes().stream().count());
            }
        });

        log.info("Counted nodes in: " + (time / 1000.0) + " s");
    }
}
