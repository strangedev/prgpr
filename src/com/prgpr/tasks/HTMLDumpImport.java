package com.prgpr.tasks;

import com.prgpr.framework.tasks.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by kito on 13/01/17.
 */
public class HTMLDumpImport extends Task {
    private static final Logger log = LogManager.getFormatterLogger(HTMLDumpImport.class);

    @Override
    public String getDescription() {
        return "Imports the HTML-File into the database.";
    }

    @Override
    public String[] getRequirements() {
        return new String[0];
    }

    @Override
    public String[] produces() {
        return new String[]{
                "Pages"
        };
    }

    @Override
    public void run() {
        log.info("test");
        /*
        PageProducer pageProducer = new PageProducer(arguments[1].get());

        ProducerLogger<Page> producerLogger = new ProducerLogger<>(true);
        producerLogger.subscribeTo(pageProducer);
        */
    }
}
