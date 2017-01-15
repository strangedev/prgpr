package com.prgpr.tasks;

import com.prgpr.PageFactory;
import com.prgpr.PageProducer;
import com.prgpr.commands.arguments.HtmlInputFileArgument;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.tasks.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by kito on 13/01/17.
 */
public class HTMLDumpImport extends Task {
    private static final Logger log = LogManager.getFormatterLogger(HTMLDumpImport.class);
    private static final int batchSize = 1000;  // Specifies the batch size for batched transactions

    protected final CommandArgument[] arguments = new CommandArgument[]{
            new HtmlInputFileArgument()
    };

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
    protected void handleArguments(String[] args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.length != 1)
            throw new InvalidNumberOfArguments();

        arguments[0].set(args[0]);
    }

    @Override
    public void run() {
        PageFactory.setDatabase(db);

        db.getTransactionManager().setBatchSize(batchSize);

        PageProducer pageProducer = new PageProducer(arguments[0].get());

        pageProducer.run();
    }
}
