package com.prgpr.tasks;

import com.prgpr.PageFactory;
import com.prgpr.PageProducer;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.transaction.DefaultTransactionManager;
import com.prgpr.framework.tasks.Task;
import com.prgpr.helpers.Benchmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by kito on 13/01/17.
 */
public class HTMLDumpImport extends Task {
    private static final Logger log = LogManager.getFormatterLogger(HTMLDumpImport.class);
    private static final int batchSize = 1000;  // Specifies the batch size for batched transactions

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
        EmbeddedDatabase db = this.context.getDb();

        PageFactory.setDatabase(db);

        db.getTransactionManager().setBatchSize(batchSize);

        PageProducer pageProducer = new PageProducer(this.context.getArgs()[0].get());

        pageProducer.run();
    }
}
