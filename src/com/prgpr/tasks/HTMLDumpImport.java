package com.prgpr.tasks;

import com.prgpr.PageFactory;
import com.prgpr.PageProducer;
import com.prgpr.commands.arguments.HtmlInputFileArgument;
import com.prgpr.data.Page;
import com.prgpr.data.TaskDependencies;
import com.prgpr.exceptions.InvalidArgument;
import com.prgpr.exceptions.InvalidNumberOfArguments;
import com.prgpr.framework.command.CommandArgument;
import com.prgpr.framework.consumer.Consumer;
import com.prgpr.framework.consumer.ConsumerProducer;
import com.prgpr.framework.consumer.Producer;
import com.prgpr.framework.tasks.Task;
import com.prgpr.framework.tasks.TaskProgressLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author Kyle Rinfreschi
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
    public TaskDependencies[] getRequirements() {
        return null;
    }

    @Override
    public TaskDependencies[] produces() {
        return new TaskDependencies[]{
                TaskDependencies.Pages
        };
    }

    @Override
    protected void handleArguments(List<String> args) throws InvalidNumberOfArguments, InvalidArgument {
        if(args.size() != 1)
            throw new InvalidNumberOfArguments();

        arguments[0].set(args.get(0));
    }

    @Override
    public void run() {
        this.subscribe(new TaskProgressLogger(true));

        PageFactory.setDatabase(db);

        db.getTransactionManager().setBatchSize(batchSize);

        PageProducer pageProducer = new PageProducer(arguments[0].get());
        pageProducer.subscribe(new PageConsumer(this));
        pageProducer.run();
    }

    private class PageConsumer extends ConsumerProducer<Page, Integer> {
        private final Producer<Integer> parent;

        PageConsumer(Producer<Integer> parent){
            this.parent = parent;
        }

        @Override
        public void consume(Page consumable) {
            parent.emit(1);
        }

        @Override
        public void onUnsubscribed(Producer<Page> producer) {
            this.parent.done();
        }
    }
}
