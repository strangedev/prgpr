package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.helpers.ProducerLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        boolean logAll = true;
        String infilePath = "";
        String outfilePath = "";

        // get and validate command line arguments
        switch (args.length){

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

        // Data processing units
        PageFactory pageFactory = new PageFactory(infilePath);
        PageExport pageExport = new PageExport(outfilePath);

        // Logging units
        ProducerLogger<Page> articleReaderLogger = new ProducerLogger<>(logAll);
        ProducerLogger<Page> pageFactoryLogger = new ProducerLogger<>(logAll);

        // Setup
        pageExport.subscribeTo(pageFactory);

        articleReaderLogger.subscribeTo(pageFactory);
        pageFactoryLogger.subscribeTo(pageFactory);

        // Execute
        pageFactory.run();
    }
}