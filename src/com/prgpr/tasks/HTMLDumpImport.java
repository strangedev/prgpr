package com.prgpr.tasks;

import com.prgpr.PageFactory;
import com.prgpr.PageProducer;
import com.prgpr.data.Page;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskContext;
import com.prgpr.helpers.ProducerLogger;
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
        PageFactory.setDatabase(this.context.getDb());

        PageProducer pageProducer = new PageProducer(this.context.getArgs()[0].get());

        ProducerLogger<Page> producerLogger = new ProducerLogger<>(true);
        producerLogger.subscribeTo(pageProducer);

        pageProducer.run();
    }
}
