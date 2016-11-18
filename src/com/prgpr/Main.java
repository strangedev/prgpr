package com.prgpr;

import com.prgpr.data.Page;
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

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).

        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }


    /**
     * Creates producer and consumer instances,
     * then subscribes them to each other.
     * Executes the PageFactory to run the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        boolean logAll = true;
        String infilePath = "";
        String outfilePath = "";

        // get and validate command line arguments
        switch (args.length){

            case 1:
                infilePath = args[0];
                break;

            case 2:
                infilePath = args[0];
                outfilePath = args[1];
                break;

            case 3:
                infilePath = args[0];
                outfilePath = args[1];

                try {
                    logAll = Boolean.parseBoolean(args[2]);

                } catch (Exception e) {
                    log.error("Invalid argument " + args[2] +  " for option log-all.");
                    System.exit(1);

                }
                break;

            default:
                log.error("Invalid number of arguments. \nPlease refer to README.txt for info on how to use.");
                System.exit(1);
        }


        File dbf = new File("neo4j/data");

        /*
        // Just for testing reasons
        if (dbf.exists()) try {
            FileUtils.deleteRecursively(dbf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbf);
        registerShutdownHook(graphDb);
        PageFactory.setDatabase(graphDb);

        PageProducer pageProducer = new PageProducer(infilePath);


        // Data processing units
        //PageFactory pageFactory = new PageFactory(infilePath);
        //PageExport pageExport = new PageExport(outfilePath);

        // Logging units
        ProducerLogger<Page> pageFactoryLogger = new ProducerLogger<>(logAll);

        // Setup
        //pageExport.subscribeTo(pageFactory);

        pageFactoryLogger.subscribeTo(pageProducer);

        // Execute
        pageProducer.run();

        try ( Transaction tx = graphDb.beginTx() ) {
            log.info(graphDb.getAllNodes().stream().count());
        }
    }
}